package com.julianduru.messingjarservice.data;

import com.julianduru.messingjarservice.entities.User;
import com.julianduru.messingjarservice.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Component;

/**
 * created by julian on 23/01/2023
 */
@Component
@RequiredArgsConstructor
public class UserDataProvider implements MongoDataProvider<User> {


    private final UserRepository userRepository;


    @Override
    public User provide() {
        var user = new User();

        user.setUsername(faker.code().isbn10());

        return user;
    }


    @Override
    public ReactiveMongoRepository<User, ObjectId> getRepository() {
        return userRepository;
    }


}

