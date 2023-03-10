package com.julianduru.messingjarservice.modules.group;

import com.julianduru.messingjarservice.ServiceConstants;
import com.julianduru.messingjarservice.entities.GroupMessage;
import com.julianduru.messingjarservice.entities.GroupUser;
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
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.time.ZonedDateTime;
import java.util.List;

/**
 * created by Julian Duru on 09/03/2023
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class GroupMessageCommandHandler implements MessageCommandHandler {

    private final GroupRepository groupRepository;

    private final UserRepository userRepository;

    private final GroupMessageRepository groupMessageRepository;

    private final GroupUserRepository groupUserRepository;

    private final UserService userService;

    private final NotificationService notificationService;



    @Override
    public Mono<OperationStatus<String>> handle(MessageCommand command) throws Exception {
        var sender = command.getUsername();
        var groupMessage = JSONUtil.fromJsonString(
            command.getPayload(), GroupMessageDto.class
        );

        var group = groupRepository.findById(new ObjectId(groupMessage.getGroupId()))
            .toFuture().join();

        if (group == null) {
            return Mono.just(
                OperationStatus.failure(
                    "Group ID %s not found".formatted(groupMessage.getGroupId())
                )
            );
        }

        var senderDetails = userRepository.findByUsername(sender)
            .toFuture().join();
        var senderFullDetails = userService.fetchUserDetails(senderDetails.getId())
            .toFuture().join();

        group.setLastMessageTimestamp(ZonedDateTime.now());
        return groupRepository.save(group)
            .map(g -> {
                var message = GroupMessage.builder()
                    .groupId(group.getId())
                    .fromUserId(senderDetails.getId())
                    .message(groupMessage.getMessage())
                    .type(groupMessage.getType())
                    .build();

                message = groupMessageRepository.save(message).toFuture().join();

                List<GroupUser> groupUsers;
                var page = 0;
                var size = 100;

                var groupMessageDto = GroupMessageDto.from(message, senderFullDetails);

                while(!(groupUsers = groupUserRepository.findGroupUsersByGroupId(
                    group.getId(), PageRequest.of(page++, size)
                ).collectList().toFuture().join()).isEmpty()) {
                    groupUsers.forEach(gu -> {
                        var groupUser = userRepository.findById(gu.getUserId())
                            .toFuture().join();
                        notificationService.writeUserNotification(
                            groupUser.getUsername(),
                            ServiceConstants.NotificationType.NEW_GROUP_MESSAGE,
                            JSONUtil.asJsonString(groupMessageDto, "")
                        );
                    });
                }

                return OperationStatus.success("Message sent");
            });
    }


    @Override
    public boolean supports(MessageCommand command) {
        return MessageCommand.Type.GROUP_MESSAGE == command.getType();
    }


}

