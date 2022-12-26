package com.julianduru.messingjarservice.repositories;

import com.julianduru.messingjarservice.entities.User;
import com.julianduru.messingjarservice.util.ReactiveBlocker;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Collection;

/**
 * created by julian on 27/08/2022
 */
@Repository
public interface UserRepository extends BaseEntityRepository<User> {


    Mono<User> findByUsername(String username);


    default boolean existsByUsername(String username) {
        var reactiveBlocker = new ReactiveBlocker<>(
            countByUsername(username).map(count -> count > 0)
        );
        return reactiveBlocker.getValue();
    }


    Mono<Long> countByUsername(String username);


    Flux<User> findByUsernameIn(Collection<String> usernames);


}
