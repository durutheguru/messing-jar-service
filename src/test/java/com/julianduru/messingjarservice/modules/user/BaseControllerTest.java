package com.julianduru.messingjarservice.modules.user;

import com.github.javafaker.Faker;
import com.julianduru.messingjarservice.config.TestDataSourceConfig;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.containers.wait.strategy.Wait;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

/**
 * created by julian on 28/08/2022
 */
@Slf4j
@Testcontainers
@ExtendWith({SpringExtension.class})
@SpringBootTest(
    classes = {
        TestDataSourceConfig.class,
    },
    webEnvironment =  SpringBootTest.WebEnvironment.RANDOM_PORT
)
@AutoConfigureMockMvc
public class BaseControllerTest {


    @Autowired
    protected MockMvc mockMvc;


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

//    static class Initializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {
//        public void initialize(ConfigurableApplicationContext configurableApplicationContext) {
//            TestPropertyValues.of(
//                "spring.data.mongodb.host", mongoDBContainer.getHost(),
//                "spring.data.mongodb.port", String.valueOf(mongoDBContainer.getFirstMappedPort())
//            ).applyTo(configurableApplicationContext.getEnvironment());
//        }
//    }


}
