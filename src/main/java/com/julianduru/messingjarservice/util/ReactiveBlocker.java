package com.julianduru.messingjarservice.util;

import lombok.extern.slf4j.Slf4j;
import org.awaitility.Awaitility;
import org.awaitility.core.ConditionTimeoutException;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * created by julian on 25/11/2022
 */
@Slf4j
public class ReactiveBlocker<T> {

    private T value;

    private AtomicBoolean valueSet;

    private Mono<T> monoPublisher;


    public ReactiveBlocker(Mono<T> publisher) {
        valueSet = new AtomicBoolean(false);
        this.monoPublisher = publisher;
    }


    public T getValue() {
        return getValue(10000, false);
    }


    public T getValue(long timeoutMillis, boolean error) {
        try {
            monoPublisher
                .map(t -> {
                    value = t;
                    valueSet.set(true);

                    return value;
                })
                .doOnError(e -> log.error(e.getMessage(), e))
                .subscribe();

            Awaitility.await()
                .atMost(Duration.ofMillis(timeoutMillis))
                .untilTrue(valueSet);

            return value;
        }
        catch (ConditionTimeoutException e) {
            if (error) {
                throw e;
            }

            return null;
        }
    }


}
