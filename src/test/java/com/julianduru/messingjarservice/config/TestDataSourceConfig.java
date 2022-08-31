package com.julianduru.messingjarservice.config;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.ContainerState;
import org.testcontainers.containers.DockerComposeContainer;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.containers.output.Slf4jLogConsumer;
import org.testcontainers.containers.wait.strategy.Wait;

import java.io.File;
import java.time.Duration;

/**
 * created by julian on 28/08/2022
 */
@Slf4j
@TestConfiguration
@ConditionalOnProperty(name = "testcontainers.enabled", havingValue = "true")
public class TestDataSourceConfig {


//    @Bean
//    public DockerComposeContainer dockerComposeContainer() {
//        var container = new DockerComposeContainer<>(
//            new File("src/test/resources/docker-compose.yml")
//        );
//        container.start();
//
//        return container;
//    }



}
