package com.julianduru.messingjarservice.docker;

import org.testcontainers.containers.DockerComposeContainer;
import org.testcontainers.containers.wait.strategy.Wait;

import java.io.File;
import java.time.Duration;

/**
 * created by julian on 04/11/2022
 */
public class ProfiledDockerComposeContainer<SELF extends DockerComposeContainer<SELF>> extends DockerComposeContainer {


    private boolean active;


    public ProfiledDockerComposeContainer(boolean active) {
        super(new File("src/test/resources/docker-compose--arm64v8.yml"));

        this.active = active;
        withExposedService("mongodb_1", 27017);
        withExposedService(
            "oauth-service_1", 10101,
            Wait.forListeningPort()
                .withStartupTimeout(Duration.ofSeconds(600))
        );
        withExposedService("kafka_1", 29092);
        withTailChildContainers(true);
    }


    public void start() {
        if (active) {
            super.start();
        }
    }


}
