package com.julianduru.messingjarservice.config;


import com.mongodb.reactivestreams.client.MongoClient;
import com.mongodb.reactivestreams.client.MongoClients;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.mongo.MongoProperties;
import org.springframework.boot.autoconfigure.mongo.embedded.EmbeddedMongoAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.core.env.Environment;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.mongodb.config.AbstractReactiveMongoConfiguration;
import org.springframework.data.mongodb.config.EnableMongoAuditing;
import org.springframework.data.mongodb.repository.config.EnableReactiveMongoRepositories;

import java.util.Optional;

/**
 * created by julian
 */
@Slf4j
@Configuration
@RequiredArgsConstructor
@EnableConfigurationProperties(MongoProperties.class)
@AutoConfigureAfter(EmbeddedMongoAutoConfiguration.class)
public class DatabaseConfiguration extends AbstractReactiveMongoConfiguration {


    private final Environment environment;


    private final MongoProperties mongoProperties;


    @Bean(destroyMethod = "close")
    public MongoClient mongoClient() {
        Integer localPort = environment.getProperty("spring.data.mongodb.port", Integer.class);

        if (localPort != null) {
            log.info("Found embedded Mongo running on port: {}.", localPort);
            return MongoClients.create(
                String.format("mongodb://localhost:%d/%s", localPort, getDatabaseName())
            );
        }

        return MongoClients.create(mongoProperties.determineUri());
    }


    @Override
    protected String getDatabaseName() {
        return mongoProperties.getMongoClientDatabase();
    }


}
