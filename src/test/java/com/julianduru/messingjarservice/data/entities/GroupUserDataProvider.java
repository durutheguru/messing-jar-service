package com.julianduru.messingjarservice.data.entities;

import com.julianduru.messingjarservice.data.MongoDataProvider;
import com.julianduru.messingjarservice.entities.GroupUser;
import com.julianduru.messingjarservice.modules.group.GroupUserRepository;
import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Component;

/**
 * created by julian on 14/02/2023
 */
@Component
@RequiredArgsConstructor
public class GroupUserDataProvider implements MongoDataProvider<GroupUser> {

    private final GroupUserRepository groupUserRepository;

    private final GroupDataProvider groupDataProvider;

    private final UserDataProvider userDataProvider;


    @Override
    public ReactiveMongoRepository<GroupUser, ObjectId> getRepository() {
        return groupUserRepository;
    }


    @Override
    public GroupUser provide() {
        try {
            var groupUser = new GroupUser();
            groupUser.setGroupId(groupDataProvider.save().toFuture().get().getId());
            groupUser.setUserId(userDataProvider.save().toFuture().get().getId());
            return groupUser;
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


}


