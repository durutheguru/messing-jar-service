package com.julianduru.messingjarservice.modules.chat;

import com.julianduru.messingjarservice.ServiceConstants;
import com.julianduru.messingjarservice.entities.BaseEntity;
import com.julianduru.messingjarservice.entities.Chat;
import com.julianduru.messingjarservice.entities.ChatMessage;
import com.julianduru.messingjarservice.entities.User;
import com.julianduru.messingjarservice.modules.chat.dto.ChatInitializationDto;
import com.julianduru.messingjarservice.modules.chat.dto.ChatInitializationResponseDto;
import com.julianduru.messingjarservice.modules.chat.dto.ChatMessageDto;
import com.julianduru.messingjarservice.modules.messaging.MessageCommand;
import com.julianduru.messingjarservice.modules.messaging.MessageCommandHandler;
import com.julianduru.messingjarservice.modules.user.NotificationService;
import com.julianduru.messingjarservice.modules.user.UserService;
import com.julianduru.messingjarservice.repositories.ChatMessageRepository;
import com.julianduru.messingjarservice.repositories.ChatRepository;
import com.julianduru.messingjarservice.repositories.UserRepository;
import com.julianduru.util.JSONUtil;
import com.julianduru.util.api.OperationStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.Comparator;
import java.util.List;

/**
 * created by julian on 21/01/2023
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class InitializeChatCommandHandler implements MessageCommandHandler {


    private final ChatRepository chatRepository;


    private final UserRepository userRepository;


    private final UserService userService;


    private final ChatMessageRepository chatMessageRepository;


    private final NotificationService notificationService;


    @Override
    public Mono<OperationStatus<String>> handle(MessageCommand command) throws Exception {
        var initiator = command.getUsername();
        var initializationRequest = JSONUtil.fromJsonString(
            command.getPayload(), ChatInitializationDto.class
        );

        var usernames = List.of(initiator, initializationRequest.getUsername());
        return userRepository.findByUsernameIn(usernames)
            .collectList()
            .flatMap(u -> {
                if (u.size() != 2) {
                    throw new IllegalStateException(
                        String.format("Cannot fetch users from datastore. %s", usernames)
                    );
                }

                var u1 = u.get(0);
                var u2 = u.get(1);

                var chat = new Chat();
                chat.setUser1(u1.getId());
                chat.setUser2(u2.getId());

                var savedChatMono = chatRepository
                    .findExistingChat(u1.getId(), u2.getId())
                    .doOnNext(existingChat -> {
                        // TODO:
                        chatMessageRepository.findByChatId(
                            existingChat.getId(),
                            PageRequest.of(0, 10, Sort.by(Sort.Direction.DESC, "createdDate"))
                        ).collectList()
                            .doOnNext(list -> {
                                if (list.isEmpty()) {
                                    return;
                                }

                                var initiatorDetails = userService.fetchUserDetails(initiator);
                                var receiverDetails = userService.fetchUserDetails(
                                    initializationRequest.getUsername()
                                );

                                Mono.zip(initiatorDetails, receiverDetails)
                                    .doOnNext(
                                        tuple -> {
                                            var response = ChatInitializationResponseDto
                                                .builder()
                                                .initiatorDetails(tuple.getT1())
                                                .receiverDetails(tuple.getT2())
                                                .history(
                                                    list.stream()
                                                        .sorted(Comparator.comparing(BaseEntity::getCreatedDate))
                                                        .map(chatMessage -> map(chatMessage, u1, u2))
                                                        .toList()
                                                )
                                                .build();

                                            notificationService.writeUserNotification(
                                                initiator,
                                                ServiceConstants.NotificationType.CHAT_HISTORY,
                                                response
                                            );
                                        }
                                    )
                                    .subscribe();
                            })
                            .subscribe();
                    })
                    .switchIfEmpty(chatRepository.save(chat));

                return savedChatMono.map(
                    m -> {
                        if (m == null) {
                            return OperationStatus.failure("Unable to save Chat");
                        }

                        log.info(
                            "Chat initiated {} <> {}",
                            m.getUser1(),
                            m.getUser2()
                        );

                        return OperationStatus.success();
                    }
                );
            });
    }


    private ChatMessageDto map(ChatMessage message, User u1, User u2) {
        var sender = message.getFromUserId().compareTo(u1.getId()) == 0  ? u1 : u2;
        var receiver = message.getToUserId().compareTo(u1.getId()) == 0 ? u1 : u2;

        return ChatMessageDto.from(message, sender, receiver);
    }


    @Override
    public boolean supports(MessageCommand command) {
        return MessageCommand.Type.INITIALIZE_CHAT == command.getType();
    }


}

