package com.julianduru.messingjarservice.config;

import com.julianduru.messingjarservice.util.AuthUtil;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.auditing.DateTimeProvider;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.domain.ReactiveAuditorAware;
import org.springframework.data.mongodb.config.EnableMongoAuditing;
import org.springframework.data.mongodb.config.EnableReactiveMongoAuditing;
import reactor.core.publisher.Mono;

import java.security.Principal;
import java.time.ZonedDateTime;
import java.util.Optional;

/**
 * created by julian on 27/08/2022
 */
@Configuration
@EnableReactiveMongoAuditing(
    auditorAwareRef = "auditorProvider",
    dateTimeProviderRef = "auditorDateTimeProvider"
)
public class AuditingConfiguration {


    @Bean
    public ReactiveAuditorAware<String> auditorProvider() {
        return () -> {
            try {
                return AuthUtil.authR().map(Principal::getName);
            }
            catch (Exception e) {
                return Mono.empty();
            }
        };
    }


    @Bean
    public DateTimeProvider auditorDateTimeProvider() {
        return () -> Optional.of(ZonedDateTime.now());
    }


}
