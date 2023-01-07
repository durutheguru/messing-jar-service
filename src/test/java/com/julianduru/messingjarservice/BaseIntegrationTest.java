package com.julianduru.messingjarservice;

import com.github.javafaker.Faker;
import com.julianduru.messingjarservice.config.OAuthServiceDatabaseConfig;
import com.julianduru.messingjarservice.config.TestConfig;
import com.julianduru.messingjarservice.docker.ProfiledDockerComposeContainer;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.testcontainers.containers.ContainerState;
import org.testcontainers.containers.DockerComposeContainer;
import org.testcontainers.junit.jupiter.Container;

/**
 * created by julian on 18/09/2022
 */
//@Testcontainers
@Slf4j
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


    private static boolean testContainersEnabled = true;


    @Container
    protected static DockerComposeContainer dockerComposeContainer = new ProfiledDockerComposeContainer(
        testContainersEnabled
    );

    static {
        if (testContainersEnabled) {
            dockerComposeContainer.start();
        }
    }


    @DynamicPropertySource
    protected static void setProperties(
        DynamicPropertyRegistry registry
    ) {
        if (testContainersEnabled) {
            setMongoProperties(registry);
            setOauthServerProperties(registry);
            setOAuthServiceDbProperties(registry);
            setKafkaProperties(registry);
        }
    }


    private static void setMongoProperties(DynamicPropertyRegistry registry) {
        var mongoHost = dockerComposeContainer.getServiceHost("mongodb_1", 27017);
        var mongoPort = dockerComposeContainer.getServicePort("mongodb_1", 27017);

        try {
            log.info("Executing Mongo replica set config");
            var container = ((ContainerState) dockerComposeContainer
                .getContainerByServiceName("mongodb_1")
                .get());

            var result = container.execInContainer(
                "/bin/bash", "-c",
            """
            mongo --eval 'printjson(rs.initiate({_id:"rs0",members:[{_id:0,host:"mongodb:27017"}]}))'
            """
            );
            container.execInContainer(
                "/bin/bash", "-c",
            """
            until mongo --eval "printjson(rs.isMaster())" | grep ismaster | grep true > /dev/null 2>&1;do sleep 1;done
            """
            );

            log.info(
                "Done executing Mongo replica set config. Exit Code: {}, StdOut: {}, StdErr: {}",
                result.getExitCode(), result.getStderr(), result.getStdout()
            );
        }
        catch (Throwable t) {
            log.error("Error while executing ReplicaSet config. " + t.getMessage(), t);
        }

        registry.add("spring.data.mongodb.host", () -> mongoHost);
        registry.add("spring.data.mongodb.port", () -> mongoPort);
    }


    private static void setOauthServerProperties(DynamicPropertyRegistry registry) {
        var oauthServiceHost = dockerComposeContainer.getServiceHost("oauth-service_1", 10101);
        var oauthServicePort = dockerComposeContainer.getServicePort("oauth-service_1", 10101);

        var oauthServiceUrl = String.format("http://%s:%d", oauthServiceHost, oauthServicePort);
        log.info("Test: OAuth Service URL: {}", oauthServiceUrl);

        registry.add(
            "code.config.oauth2.authorization-server.base-url",
            () -> oauthServiceUrl
        );
        registry.add(
            "code.config.oauth2.authorization-server.gql-base-url",
            () -> String.format("%s/graphql", oauthServiceUrl)
        );
    }


    private static void setOAuthServiceDbProperties(DynamicPropertyRegistry registry) {
        var oauthServiceDbHost = dockerComposeContainer.getServiceHost("mysqldb_1", 33080);
        var oauthServiceDbPort = dockerComposeContainer.getServicePort("mysqldb_1", 33080);

        registry.add(
            "test.code.config.oauth-service.datasource.url",
            () -> String.format(
                "jdbc:mysql://%s:%d/oauth_service?createDatabaseIfNotExist=true&serverTimezone=UTC",
                oauthServiceDbHost, 33080
            )
        );
        registry.add(
            "test.code.config.oauth-service.datasource.username",
            () -> "root"
        );
        registry.add(
            "test.code.config.oauth-service.datasource.password",
            () -> "1234567890"
        );
    }


    private static void setKafkaProperties(DynamicPropertyRegistry registry) {
//        var kafkaHost = dockerComposeContainer.getServiceHost("kafka_1", 29092);
//        var kafkaPort = dockerComposeContainer.getServicePort("kafka_1", 29092);
//
//        var kafkaUrl = String.format("%s:%d", kafkaHost, kafkaPort);
//
//        registry.add(
//            "spring.kafka.bootstrap-servers",
//            () -> kafkaUrl
//        );
    }


}


