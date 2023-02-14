package com.julianduru.messingjarservice.modules.group;

import com.julianduru.messingjarservice.entities.Group;
import com.julianduru.messingjarservice.BaseEntityRepository;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.Update;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

import java.time.ZonedDateTime;

/**
 * created by julian on 10/02/2023
 */
@Repository
public interface GroupRepository extends BaseEntityRepository<Group> {



    @Query("{ '_id' : ?0 }")
    @Update("{ '$set' : { 'lastMessageTimestamp' : ?1 } }")
    Mono<Long> updateLastMessageTimeStampById(ObjectId objectId, ZonedDateTime timestamp);



}
