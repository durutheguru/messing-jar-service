package com.julianduru.messingjarservice.modules.group;

import com.julianduru.messingjarservice.ServiceConstants;
import com.julianduru.messingjarservice.entities.BaseEntity;
import com.julianduru.messingjarservice.entities.GroupMessage;
import com.julianduru.messingjarservice.entities.User;
import com.julianduru.messingjarservice.modules.group.dto.GroupInitializationDto;
import com.julianduru.messingjarservice.modules.group.dto.GroupInitializationResponseDto;
import com.julianduru.messingjarservice.modules.group.dto.GroupMessageDto;
import com.julianduru.messingjarservice.modules.messaging.MessageCommand;
import com.julianduru.messingjarservice.modules.messaging.MessageCommandHandler;
import com.julianduru.messingjarservice.modules.user.NotificationService;
import com.julianduru.messingjarservice.modules.user.UserRepository;
import com.julianduru.messingjarservice.modules.user.UserService;
import com.julianduru.util.JSONUtil;
import com.julianduru.util.api.OperationStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.Comparator;
import java.util.HashSet;
import java.util.stream.Collectors;

/**
 * created by Julian Duru on 09/03/2023
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class InitializeGroupCommandHandler implements MessageCommandHandler {


    private final GroupRepository groupRepository;


    private final UserRepository userRepository;


    private final UserService userService;


    private final GroupMessageRepository groupMessageRepository;


    private final NotificationService notificationService;



    @Override
    public Mono<OperationStatus<String>> handle(MessageCommand command) throws Exception {
        var initiator = command.getUsername();
        var initializationRequest = JSONUtil.fromJsonString(
            command.getPayload(), GroupInitializationDto.class
        );

        return userRepository.findByUsername(initiator)
            .map(u -> {
                var group = groupRepository.findById(
                    new ObjectId(initializationRequest.getGroupId())
                ).toFuture().join();

                if (group == null) {
                    return OperationStatus.failure("Group not found");
                }

                groupMessageRepository.findByGroupId(
                    group.getId(), PageRequest.of(0, 10, Sort.by(Sort.Direction.DESC, "createdDate"))
                ).collectList()
                    .doOnNext(messages -> {
                        var senderUsernames = userRepository
                            .findAllById(
                                messages.stream()
                                    .map(GroupMessage::getFromUserId)
                                    .collect(Collectors.toSet())
                            )
                            .collectList()
                            .toFuture()
                            .join()
                            .stream()
                            .map(User::getUsername)
                            .toList();

                        var usernamesSet = new HashSet<>(senderUsernames);
                        usernamesSet.add(initiator);

                        var userDetails = userService.fetchUserDetails(usernamesSet)
                            .collectList().toFuture().join();

                        var response = GroupInitializationResponseDto.builder()
                            .initiatorDetails(
                                userDetails.stream()
                                    .filter(usr -> usr.getUsername().equals(initiator))
                                    .findFirst().orElse(null)
                            )
                            .otherMembersDetails(
                                userDetails.stream()
                                    .filter(usr -> !usr.getUsername().equals(initiator))
                                    .toList()
                            )
                            .history(
                                messages.stream()
                                    .sorted(Comparator.comparing(BaseEntity::getCreatedDate))
                                    .map(GroupMessageDto::from)
                                    .toList()
                            )
                            .build();

                        notificationService.writeUserNotification(
                            initiator, ServiceConstants.NotificationType.GROUP_HISTORY, response
                        );

                    }).subscribe();


                return OperationStatus.success();
            })
            .switchIfEmpty(Mono.just(OperationStatus.failure("User not found")));
    }


    @Override
    public boolean supports(MessageCommand command) {
        return MessageCommand.Type.INITIALIZE_GROUP == command.getType();
    }


}

