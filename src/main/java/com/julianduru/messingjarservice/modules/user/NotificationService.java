package com.julianduru.messingjarservice.modules.user;

import com.julianduru.messingjarservice.modules.kafka.PushRegistry;
import com.julianduru.queueintegrationlib.module.publish.OutgoingMessagePublisher;
import com.julianduru.util.JSONUtil;
import com.julianduru.util.api.OperationStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * created by julian on 07/12/2022
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class NotificationService {


    @Value("${code.config.kafka.user-push-notification.topic-name}")
    private String pushNotificationTopicName;


    private final OutgoingMessagePublisher messagePublisher;


    private final PushRegistry pushRegistry;



    public <T> OperationStatus<String> writeUserNotification(String username, String notificationType, T data) {
        try {
            var nodeIds = pushRegistry.getUserConnectedNodeIds(username);
            if (nodeIds.isEmpty()) {
                log.debug("User has no connected nodes");
                return OperationStatus.success();
            }

            for (String nodeId : nodeIds) {
                messagePublisher.publish(
                    Map.of(
                        "UserID", username,
                        "NotificationType", notificationType
                    ),
                    nodeId, data, true
                );
            }

            return OperationStatus.success();
        }
        catch (Throwable t) {
            log.error(t.getMessage(), t);
            return OperationStatus.failure(t.getMessage());
        }
    }


}
