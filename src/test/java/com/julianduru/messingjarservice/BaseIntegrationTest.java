package com.julianduru.messingjarservice;

import com.github.javafaker.Faker;
import com.julianduru.messingjarservice.config.TestConfig;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.testcontainers.containers.DockerComposeContainer;
import org.testcontainers.containers.wait.strategy.Wait;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.io.File;
import java.time.Duration;

/**
 * created by julian on 18/09/2022
 */
@Testcontainers
@ExtendWith({SpringExtension.class})
@SpringBootTest(
    classes = {
        TestConfig.class,
    },
    webEnvironment =  SpringBootTest.WebEnvironment.RANDOM_PORT
)
public class BaseIntegrationTest {


    protected Faker faker = new Faker();


    @Container
    protected static DockerComposeContainer dockerComposeContainer = new DockerComposeContainer(
        new File("src/test/resources/docker-compose.yml")
    )
        .withExposedService("mongodb_1", 27017)
        .withExposedService(
            "oauth-service_1", 10101,
            Wait.forHttp("/")
                .forStatusCodeMatching(code -> code >= 200 && code <= 500)
                .withStartupTimeout(Duration.ofSeconds(300))
        )
        .withExposedService("eureka-discovery-server_1", 8761)
        .withEnv("DOCKER_DEFAULT_PLATFORM", "linux/amd64")
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


}
