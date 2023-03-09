package com.julianduru.messingjarservice.data.entities;

import com.julianduru.messingjarservice.data.MongoDataProvider;
import com.julianduru.messingjarservice.entities.Chat;
import com.julianduru.messingjarservice.modules.chat.ChatRepository;
import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Component;

import java.util.UUID;

/**
 * created by julian on 25/01/2023
 */
@Component
@RequiredArgsConstructor
public class ChatProvider implements MongoDataProvider<Chat> {

    private final ChatRepository chatRepository;


    @Override
    public ReactiveMongoRepository<Chat, ObjectId> getRepository() {
        return chatRepository;
    }


    @Override
    public Chat provide() {
        var chat = new Chat();

        chat.setUser1(new ObjectId(UUID.randomUUID().toString()));
        chat.setUser2(new ObjectId(UUID.randomUUID().toString()));

        return chat;
    }


}