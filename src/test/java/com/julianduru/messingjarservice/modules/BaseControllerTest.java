package com.julianduru.messingjarservice.modules;

import com.github.javafaker.Faker;
import com.julianduru.messingjarservice.config.TestConfig;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.codec.ClientCodecConfigurer;
import org.springframework.security.oauth2.client.ReactiveOAuth2AuthorizedClientManager;
import org.springframework.security.oauth2.client.web.reactive.function.client.ServerOAuth2AuthorizedClientExchangeFilterFunction;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.testcontainers.containers.DockerComposeContainer;
import org.testcontainers.containers.output.Slf4jLogConsumer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.io.File;

/**
 * created by julian on 28/08/2022
 */
@Slf4j
@Testcontainers
@ExtendWith({SpringExtension.class})
@SpringBootTest(
    classes = {
        TestConfig.class,
    },
    webEnvironment =  SpringBootTest.WebEnvironment.RANDOM_PORT
)
public class BaseControllerTest {


    @Autowired
    protected WebTestClient webTestClient;


    @Autowired
    private ReactiveOAuth2AuthorizedClientManager manager;


    protected Faker faker = new Faker();


    private boolean webTestClientInitialized = false;


    @Container
    protected static DockerComposeContainer dockerComposeContainer = new DockerComposeContainer(
        new File("src/test/resources/docker-compose.yml")
    )
        .withExposedService("mongodb_1", 27017)
        .withExposedService("oauth-service_1", 10101)
        .withExposedService("eureka-discovery-server_1", 8761)
        .withEnv("DOCKER_DEFAULT_PLATFORM", "linux/amd64")
        .withLocalCompose(true)
        .withTailChildContainers(true);


    @DynamicPropertySource
    protected static void setProperties(
        DynamicPropertyRegistry registry
    ) {
        setMongoProperties(registry);
        setOauthServerProperties(registry);
    }


    private static void setMongoProperties(DynamicPropertyRegistry registry) {
        var mongoHost = dockerComposeContainer.getServiceHost("mongodb_1", 27017);
        var mongoPort = dockerComposeContainer.getServicePort("mongodb_1", 27017);

        registry.add("spring.data.mongodb.host", () -> mongoHost);
        registry.add("spring.data.mongodb.port", () -> mongoPort);
    }


    private static void setOauthServerProperties(DynamicPropertyRegistry registry) {
        var oauthServiceHost = dockerComposeContainer.getServiceHost("oauth-service_1", 10101);
        var oauthServicePort = dockerComposeContainer.getServicePort("oauth-service_1", 10101);

        var oauthServiceUrl = String.format("%s:%d", oauthServiceHost, oauthServicePort);

        registry.add(
            "code.config.oauth2.authorization-server.base-url",
            () -> oauthServiceUrl
        );
        registry.add(
            "code.config.oauth2.authorization-server.gql-base-url",
            () -> String.format("%s/graphql", oauthServiceUrl)
        );
    }


    @BeforeEach
    public void beforeClass() {
        initializeWebTestClient();
    }


    private void initializeWebTestClient() {
        if (webTestClientInitialized) {
            log.info("Web Test Client already initialized");
            return;
        }

        var exchangeStrategies =
            ExchangeStrategies.builder()
                .codecs(ClientCodecConfigurer::defaultCodecs)
                .build();
        var oauth = new ServerOAuth2AuthorizedClientExchangeFilterFunction(manager);
        oauth.setDefaultClientRegistrationId("messing-jar-service");

        webTestClient = webTestClient.mutate()
            .filter(oauth)
            .exchangeStrategies(exchangeStrategies)
            .build();

        webTestClientInitialized = true;
    }


}



