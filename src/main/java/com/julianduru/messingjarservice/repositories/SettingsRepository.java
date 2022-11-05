package com.julianduru.messingjarservice.repositories;

import com.julianduru.messingjarservice.entities.Settings;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

import java.util.Optional;

/**
 * created by julian on 01/11/2022
 */
@Repository
public interface SettingsRepository extends BaseEntityRepository<Settings> {


    Mono<Settings> findByUsername(String username);


}
