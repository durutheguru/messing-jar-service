package com.julianduru.messingjarservice.repositories;

import com.julianduru.messingjarservice.entities.User;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

/**
 * created by julian on 27/08/2022
 */
@Repository
public interface UserRepository extends BaseEntityRepository<User> {


    Mono<User> findByUsername(String username);


}
