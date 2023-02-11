package com.julianduru.messingjarservice.config;

import com.julianduru.oauthservicelib.component.MutatingReactiveClientRegistrationRepository;
import com.julianduru.oauthservicelib.config.WebClientOAuthConfigurer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.security.oauth2.client.OAuth2ClientProperties;
import org.springframework.boot.autoconfigure.security.oauth2.client.OAuth2ClientPropertiesRegistrationAdapter;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.security.oauth2.client.registration.ReactiveClientRegistrationRepository;
import org.springframework.web.reactive.function.client.WebClient;
import org.testcontainers.containers.DockerComposeContainer;
import org.zalando.logbook.DefaultHttpLogWriter;
import org.zalando.logbook.HttpLogWriter;

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



    @Bean
    public HttpLogWriter zalandoLogWriter() {
        return new DefaultHttpLogWriter();
    }


//    @Bean
//    public WebClient oauthServerWebClient(
//        @Value("${code.config.oauth2.authorization-server.base-url}") String oauthServerUrl,
//        WebClientOAuthConfigurer webClientOAuthConfigurer
//    ) {
//        return webClientOAuthConfigurer.configureWebClient(oauthServerUrl);
//    }


//    @Bean
//    @ConditionalOnBean(DockerComposeContainer.class)
//    public WebClient oauthServerGQLWebClient(
//        DockerComposeContainer dockerComposeContainer,
//        WebClientOAuthConfigurer webClientOAuthConfigurer
//    ) {
//        String oauthServiceUrl = "";
//
//        if (dockerComposeContainer != null) {
//            oauthServiceUrl = String.format(
//                "%s:%s/graphql",
//                dockerComposeContainer.getServiceHost("oauth-service_1", 10101),
//                dockerComposeContainer.getServicePort("oauth-service_1", 10101)
//            );
//        }
//        else {
//
//        }
//        log.info("OAuth Service URL: {}", oauthServiceUrl);
//
//
//        return webClientOAuthConfigurer.configureWebClient(oauthServiceUrl);
//    }


}
