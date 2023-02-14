package com.julianduru.messingjarservice.modules.group;

import com.julianduru.messingjarservice.entities.GroupUser;
import com.julianduru.messingjarservice.BaseEntityRepository;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * created by julian on 21/01/2023
 */
@Repository
public interface GroupUserRepository extends BaseEntityRepository<GroupUser> {


    Mono<GroupUser> findByGroupIdAndUserId(ObjectId groupId, ObjectId userId);


    Flux<GroupUser> findGroupUsersByGroupId(ObjectId groupId);


    Flux<GroupUser> findGroupUsersByUserId(ObjectId userId);


}

