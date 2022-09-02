package com.julianduru.messingjarservice.modules.user;

import com.github.javafaker.Faker;
import com.julianduru.messingjarservice.config.TestConfig;
import com.julianduru.messingjarservice.config.TestDataSourceConfig;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.containers.wait.strategy.Wait;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import reactor.core.publisher.Mono;

/**
 * created by julian on 28/08/2022
 */
@Slf4j
@Testcontainers
@ExtendWith({SpringExtension.class})
@SpringBootTest(
    classes = {
        TestConfig.class,
        TestDataSourceConfig.class,
    },
    webEnvironment =  SpringBootTest.WebEnvironment.RANDOM_PORT
)
public class BaseControllerTest {


    @Autowired
    protected WebTestClient webTestClient;


    protected Faker faker = new Faker();


    @Container
    protected static MongoDBContainer mongoDBContainer = new MongoDBContainer("mongo");


    @DynamicPropertySource
    static void setMongoProperties(
        DynamicPropertyRegistry registry
    ) {
        registry.add("spring.data.mongodb.host", mongoDBContainer::getHost);
        registry.add("spring.data.mongodb.port", mongoDBContainer::getFirstMappedPort);

        mongoDBContainer.waitingFor(Wait.forListeningPort());
    }


}
