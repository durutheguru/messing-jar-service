package com.julianduru.messingjarservice.modules.messaging;


import com.julianduru.messingjarservice.util.ReactiveBlocker;
import com.julianduru.util.JSONUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.config.KafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.listener.ConcurrentMessageListenerContainer;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * created by julian on 29/11/2022
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class MessageCommandConsumer {


    private final KafkaProperties kafkaProperties;


    @Autowired
    private List<MessageCommandHandler> commandHandlers;


    private Map<String, Object> consumerConfigs() {
        var props = kafkaProperties.buildConsumerProperties();
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        return props;
    }


    private ConsumerFactory<String, Object> consumerFactory() {
        return new DefaultKafkaConsumerFactory<>(consumerConfigs());
    }


    @Bean
    public KafkaListenerContainerFactory<ConcurrentMessageListenerContainer<String, Object>>
    kafkaUserCommandListenerContainerFactory() {
        var factory = new ConcurrentKafkaListenerContainerFactory<String, Object>();
        factory.setConsumerFactory(consumerFactory());
        return factory;
    }


    @KafkaListener(
        topics = {"user-command"},
        groupId = "userCommandListenerGroup"
    )
    public void onMessage(ConsumerRecord<String, String> record) {
        try {
            log.info("User Command Consumer Record: {}", record);

            var value = record.value();
            var command = JSONUtil.fromJsonString(value, MessageCommand.class);

            handleCommand(command);
        } catch (IOException e) {
            log.error("Error while reading Message Command", e);
        }
    }


    private void handleCommand(MessageCommand command) {
        for (var handler : commandHandlers) {
            try {
                if (!handler.supports(command)) {
                    continue;
                }

                log.debug("Handler {} handling command type {}", handler.getClass().getName(), command.getType());
                var response = new ReactiveBlocker<>(handler.handle(command)).getValue();
                log.debug("Command Response: {}", response);
            } catch (Throwable t) {
                log.error(t.getMessage(), t);
            }
        }
    }

}

