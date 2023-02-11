package com.julianduru.messingjarservice.modules.user;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.julianduru.fileuploader.repositories.FileUploadRepository;
import com.julianduru.kafkaintegrationlib.KafkaWriter;
import com.julianduru.messingjarservice.ServiceConstants;
import com.julianduru.messingjarservice.modules.user.dto.UserDataDto;
import com.julianduru.util.JSONUtil;
import com.julianduru.util.api.OperationStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.UUID;

/**
 * created by julian on 07/12/2022
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class NotificationService {


    @Value("${code.config.kafka.user-push-notification.topic-name}")
    private String pushNotificationTopicName;


    private final KafkaWriter writer;


    private final KafkaTemplate<String, String> messageProducerKafkaTemplate;



    public <T> OperationStatus<String> writeUserNotification(String username, String notificationType, T data) {
        try {
            writer.write(
                messageProducerKafkaTemplate,
                pushNotificationTopicName,
                UUID.randomUUID().toString(),
                String.format(
                    "%s|%s|%s",
                    username,
                    notificationType,
                    JSONUtil.asJsonString(data)
                )
            );

            return OperationStatus.success();
        }
        catch (Throwable t) {
            log.error(t.getMessage(), t);
            return OperationStatus.failure(t.getMessage());
        }
    }


}
