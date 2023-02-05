package com.julianduru.messingjarservice.modules.chat;

import com.julianduru.messingjarservice.ServiceConstants;
import com.julianduru.messingjarservice.entities.ChatMessage;
import com.julianduru.messingjarservice.modules.chat.dto.ChatMessageDto;
import com.julianduru.messingjarservice.modules.messaging.MessageCommand;
import com.julianduru.messingjarservice.modules.messaging.MessageCommandHandler;
import com.julianduru.messingjarservice.modules.user.NotificationService;
import com.julianduru.messingjarservice.repositories.ChatMessageRepository;
import com.julianduru.messingjarservice.repositories.ChatRepository;
import com.julianduru.messingjarservice.repositories.UserRepository;
import com.julianduru.util.JSONUtil;
import com.julianduru.util.api.OperationStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.List;

/**
 * created by julian on 25/01/2023
 */

@Slf4j
@Component
@RequiredArgsConstructor
public class ChatMessageCommandHandler implements MessageCommandHandler {


    private final ChatRepository chatRepository;


    private final UserRepository userRepository;


    private final ChatMessageRepository chatMessageRepository;


    private final NotificationService notificationService;


    @Override
    public Mono<OperationStatus<String>> handle(MessageCommand command) throws Exception {
        var senderUsername = command.getUsername();
        var chatMessageRequest = JSONUtil.fromJsonString(
            command.getPayload(), ChatMessageDto.class
        );
        var receiverUsername = chatMessageRequest.getTo();
        var usernames = List.of(senderUsername, receiverUsername);

        return userRepository.findByUsernameIn(usernames)
            .collectList()
            .flatMap(u -> {
                if (u.size() != 2) {
                    throw new IllegalStateException(
                        String.format("Cannot fetch users from datastore. %s", usernames)
                    );
                }

                var sender = u.stream().filter(
                    usr -> usr.getUsername().equalsIgnoreCase(senderUsername)
                ).findFirst().orElseThrow();

                var receiver = u.stream().filter(
                    usr -> usr.getUsername().equalsIgnoreCase(receiverUsername)
                ).findFirst().orElseThrow();

                return chatRepository
                    .findExistingChat(sender.getId(), receiver.getId())
                    .switchIfEmpty(Mono.error(new IllegalStateException("Cannot find chat between the users")))
                    .flatMap(chat -> {
                        chat.setLastMessageTime(LocalDateTime.now());
                        return chatRepository.save(chat);
                    })
                    .map(chat -> {
                        var chatMessage = new ChatMessage();

                        chatMessage.setFromUserId(sender.getId());
                        chatMessage.setToUserId(receiver.getId());
                        chatMessage.setMessage(chatMessageRequest.getMessage());
                        chatMessage.setChatId(chat.getId());
                        chatMessage.setType(chatMessageRequest.getType());

                        chatMessageRepository.save(chatMessage)
                            .doOnNext(cm -> {
                                var notification = JSONUtil.asJsonString(
                                    ChatMessageDto.from(cm, sender, receiver), ""
                                );

                                notificationService.writeUserNotification(
                                    receiverUsername,
                                    ServiceConstants.NotificationType.NEW_CHAT_MESSAGE,
                                    notification
                                );

                                notificationService.writeUserNotification(
                                    senderUsername,
                                    ServiceConstants.NotificationType.NEW_CHAT_MESSAGE,
                                    notification
                                );
                            }).subscribe();

                        return OperationStatus.success();
                    })
                    .onErrorMap(e -> {
                        log.error(e.getMessage(), e);
                        return e;
                    });
            });
    }


    @Override
    public boolean supports(MessageCommand command) {
        return MessageCommand.Type.CHAT_MESSAGE == command.getType();
    }


}

