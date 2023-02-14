package com.julianduru.messingjarservice.data.entities;

import com.julianduru.messingjarservice.data.MongoDataProvider;
import com.julianduru.messingjarservice.entities.ChatMessageType;
import com.julianduru.messingjarservice.entities.GroupMessage;
import com.julianduru.messingjarservice.modules.group.GroupMessageRepository;
import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Component;

import java.util.UUID;

/**
 * created by julian on 14/02/2023
 */

@Component
@RequiredArgsConstructor
public class GroupMessageDataProvider implements MongoDataProvider<GroupMessage> {


    private final GroupDataProvider groupDataProvider;


    private final GroupMessageRepository groupMessageRepository;


    private final UserDataProvider userDataProvider;


    @Override
    public ReactiveMongoRepository<GroupMessage, ObjectId> getRepository() {
        return groupMessageRepository;
    }


    @Override
    public GroupMessage provide() {
        var message = new GroupMessage();

        message.setGroupId(groupDataProvider.save().toFuture().join().getId());
        message.setFromUserId(userDataProvider.save().toFuture().join().getId());
        message.setMessage(faker.lorem().paragraph());
        message.setType(ChatMessageType.TEXT);

        return message;
    }


}

