package com.julianduru.messingjarservice.repositories;

import com.julianduru.messingjarservice.entities.Chat;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * created by julian on 21/01/2023
 */
@Repository
public interface ChatRepository extends BaseEntityRepository<Chat> {


    Mono<Chat> findByUser1AndUser2(ObjectId user1, ObjectId user2);


    default Mono<Chat> findExistingChat(ObjectId user1, ObjectId user2) {
        assertThat(user1).isNotNull();
        assertThat(user2).isNotNull();

        return findByUser1AndUser2(user1, user2)
            .mergeWith(findByUser1AndUser2(user2, user1))
            .doOnNext(c -> System.out.println(c.toString()))
            .singleOrEmpty();
    }


}
