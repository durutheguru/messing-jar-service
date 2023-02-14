package com.julianduru.messingjarservice.data;

import com.julianduru.messingjarservice.entities.ChatMessage;
import com.julianduru.messingjarservice.modules.chat.ChatMessageRepository;
import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Component;

/**
 * created by julian on 25/01/2023
 */
@Component
@RequiredArgsConstructor
public class ChatMessageProvider implements MongoDataProvider<ChatMessage> {


    private final ChatMessageRepository chatMessageRepository;


    @Override
    public ReactiveMongoRepository<ChatMessage, ObjectId> getRepository() {
        return chatMessageRepository;
    }


    @Override
    public ChatMessage provide() {
        var message = new ChatMessage();

        message.setChatId(new ObjectId(faker.random().hex(24)));
        message.setFromUserId(new ObjectId(faker.random().hex(24)));
        message.setToUserId(new ObjectId(faker.random().hex(24)));
        message.setMessage(faker.lorem().paragraph());

        return message;
    }


}

