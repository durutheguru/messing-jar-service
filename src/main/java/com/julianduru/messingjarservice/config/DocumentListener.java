package com.julianduru.messingjarservice.config;

import com.julianduru.messingjarservice.entities.BaseEntity;
import org.bson.Document;
import org.reactivestreams.Publisher;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.mapping.event.ReactiveBeforeConvertCallback;
import org.springframework.data.mongodb.core.mapping.event.ReactiveBeforeSaveCallback;
import reactor.core.publisher.Mono;

import java.util.UUID;

/**
 * created by julian on 31/08/2022
 */
@Configuration
public class DocumentListener implements ReactiveBeforeConvertCallback<BaseEntity> {


    @Override
    public Publisher<BaseEntity> onBeforeConvert(BaseEntity entity, String collection) {
        entity.setCode(
            String.format(
                "%s-%s",
                UUID.randomUUID().toString(),
                UUID.randomUUID().toString()
            )
        );

        return Mono.just(entity);
    }


}
