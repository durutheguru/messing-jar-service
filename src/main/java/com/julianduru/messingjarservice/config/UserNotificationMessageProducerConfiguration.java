package com.julianduru.messingjarservice.config;

import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * created by julian on 07/12/2022
 */
@Configuration
@RequiredArgsConstructor
public class UserNotificationMessageProducerConfiguration {


    private final KafkaProperties kafkaProperties;


    @Value("${code.config.kafka.user-push-notification.topic-name}")
    private String pushNotificationTopicName;


    @Bean
    public Map<String, Object> userNotificationProducerConfigs() {
        var props = new HashMap<>(kafkaProperties.buildProducerProperties());
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        props.put(ProducerConfig.MAX_BLOCK_MS_CONFIG, 5000);
        return props;
    }


    @Bean
    public ProducerFactory<String, String> userNotificationProducerFactory() {
        return new DefaultKafkaProducerFactory<>(userNotificationProducerConfigs());
    }


    @Bean
    public KafkaTemplate<String, String> userNotificationKafkaTemplate() {
        return new KafkaTemplate<>(userNotificationProducerFactory());
    }


    @Bean
    public NewTopic pushNotificationTopic() {
        return new NewTopic(pushNotificationTopicName, 3, (short) 1);
    }


}


