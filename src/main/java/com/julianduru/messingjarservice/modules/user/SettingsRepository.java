package com.julianduru.messingjarservice.modules.user;

import com.julianduru.messingjarservice.entities.Settings;
import com.julianduru.messingjarservice.BaseEntityRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

/**
 * created by julian on 01/11/2022
 */
@Repository
public interface SettingsRepository extends BaseEntityRepository<Settings> {


    Mono<Settings> findByUsername(String username);


}
