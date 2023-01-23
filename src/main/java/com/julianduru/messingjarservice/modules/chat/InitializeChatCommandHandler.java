package com.julianduru.messingjarservice.modules.chat;

import com.julianduru.messingjarservice.entities.Chat;
import com.julianduru.messingjarservice.modules.chat.dto.ChatInitialization;
import com.julianduru.messingjarservice.modules.messaging.MessageCommand;
import com.julianduru.messingjarservice.modules.messaging.MessageCommandHandler;
import com.julianduru.messingjarservice.repositories.ChatRepository;
import com.julianduru.messingjarservice.repositories.UserRepository;
import com.julianduru.util.JSONUtil;
import com.julianduru.util.api.OperationStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

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


    @Override
    public Mono<OperationStatus<String>> handle(MessageCommand command) throws Exception {
        var initiator = command.getUsername();
        var initializationRequest = JSONUtil.fromJsonString(
            command.getPayload(), ChatInitialization.class
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

                var chat = new Chat();
                chat.setUser1(u.get(0));
                chat.setUser2(u.get(1));

                var savedChatMono = chatRepository.save(chat);
                return savedChatMono.map(
                    m -> {
                        if (m == null) {
                            return OperationStatus.failure("Unable to save Chat");
                        }

                        log.info(
                            "New Chat initiated {} <> {}",
                            m.getUser1().getUsername(),
                            m.getUser2().getUsername()
                        );

                        return OperationStatus.success();
                    }
                );
            });
    }


    @Override
    public boolean supports(MessageCommand command) {
        return MessageCommand.Type.INITIALIZE_CHAT == command.getType();
    }


}

