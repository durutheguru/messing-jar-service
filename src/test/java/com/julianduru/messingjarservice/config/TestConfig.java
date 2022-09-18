package com.julianduru.messingjarservice.config;

import com.julianduru.oauthservicelib.component.MutatingReactiveClientRegistrationRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.security.oauth2.client.OAuth2ClientProperties;
import org.springframework.boot.autoconfigure.security.oauth2.client.OAuth2ClientPropertiesRegistrationAdapter;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.security.oauth2.client.registration.ReactiveClientRegistrationRepository;

/**
 * created by julian on 31/08/2022
 */
@Slf4j
@TestConfiguration
public class TestConfig {


    @Bean
    public ReactiveClientRegistrationRepository reactiveClientRegistrationRepository(
        OAuth2ClientProperties clientProperties
    ) {
        var clientRegistrations = OAuth2ClientPropertiesRegistrationAdapter
            .getClientRegistrations(clientProperties).values();

        return new MutatingReactiveClientRegistrationRepository(
            clientRegistrations.stream().toList()
        );
    }


}
