package com.julianduru.messingjarservice.modules.kafka;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

/**
 * created by Julian Duru on 04/03/2023
 */
@Service
@RequiredArgsConstructor
public class PushRegistry {


    private final StringRedisTemplate redisTemplate;



    public List<String> getUserConnectedNodeIds(String userId) {
        var nodeIds = redisTemplate.opsForValue().get(userId);
        if (nodeIds == null) {
            return List.of();
        }

        return Arrays.stream(nodeIds.split("\\s*,\\s*")).toList();
    }


    public String getClientConnectedNodeId(String clientId) {
        return redisTemplate.opsForValue().get(clientId);
    }


}
