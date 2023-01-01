package com.julianduru.messingjarservice.modules.kafka;

import com.julianduru.kafkaintegrationlib.FailedMessage;
import com.julianduru.kafkaintegrationlib.FailedMessageHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

/**
 * created by julian on 03/11/2022
 */
@Slf4j
@Component
@Primary
public class MessingJarFailedMessageHandler implements FailedMessageHandler {


    @Override
    public void saveFailedMessage(FailedMessage<?> message) {
        log.info("MessingJar saved message handling..");
    }

    @Override
    public Page<FailedMessage<?>> readFailedMessages(int page, int size) {
        log.info("Reading MessingJar saved message..");
        return null;
    }


}
