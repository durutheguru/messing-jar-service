package com.julianduru.messingjarservice.data;

import com.julianduru.messingjarservice.entities.Group;
import com.julianduru.messingjarservice.modules.group.GroupRepository;
import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Component;

/**
 * created by julian on 10/02/2023
 */
@Component
@RequiredArgsConstructor
public class GroupDataProvider implements MongoDataProvider<Group> {


    private final GroupRepository groupRepository;


    @Override
    public ReactiveMongoRepository<Group, ObjectId> getRepository() {
        return groupRepository;
    }


    @Override
    public Group provide() {
        var group = new Group();

        group.setName(faker.name().fullName() + " Group");
        group.setIconImageRef(faker.code().isbn10());

        return group;
    }


}

