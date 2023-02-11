package com.julianduru.messingjarservice.repositories;

import com.julianduru.messingjarservice.entities.GroupUser;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

/**
 * created by julian on 21/01/2023
 */
@Repository
public interface GroupUserRepository extends BaseEntityRepository<GroupUser> {


    Mono<GroupUser> findByGroupIdAndUserId(ObjectId groupId, ObjectId userId);


}
