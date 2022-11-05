package com.julianduru.messingjarservice;

import com.github.javafaker.Faker;
import com.julianduru.messingjarservice.config.OAuthServiceDatabaseConfig;
import com.julianduru.messingjarservice.config.TestConfig;
import com.julianduru.messingjarservice.docker.ProfiledDockerComposeContainer;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.testcontainers.containers.DockerComposeContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

/**
 * created by julian on 18/09/2022
 */
@Testcontainers
@ExtendWith({SpringExtension.class})
@SpringBootTest(
    classes = {
        TestConfig.class,
        OAuthServiceDatabaseConfig.class,
    },
    webEnvironment =  SpringBootTest.WebEnvironment.RANDOM_PORT
)
public class BaseIntegrationTest {


    protected Faker faker = new Faker();


    private static boolean testContainersEnabled = "true"
        .equalsIgnoreCase(System.getenv("testcontainers.enabled"));


    @Container
    protected static DockerComposeContainer dockerComposeContainer = new ProfiledDockerComposeContainer(
        testContainersEnabled
    );


    @DynamicPropertySource
    protected static void setProperties(
        DynamicPropertyRegistry registry
    ) {
        if (testContainersEnabled) {
            setMongoProperties(registry);
            setOauthServerProperties(registry);
            setKafkaProperties(registry);
        }
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


    private static void setKafkaProperties(DynamicPropertyRegistry registry) {
        var kafkaHost = dockerComposeContainer.getServiceHost("kafka_1", 29092);
        var kafkaPort = dockerComposeContainer.getServicePort("kafka_1", 29092);

        var kafkaUrl = String.format("%s:%d", kafkaHost, kafkaPort);

        registry.add(
            "spring.kafka.bootstrap-servers",
            () -> kafkaUrl
        );
    }


}


