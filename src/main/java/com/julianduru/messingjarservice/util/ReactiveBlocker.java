package com.julianduru.messingjarservice.util;

import org.awaitility.Awaitility;
import org.awaitility.core.ConditionTimeoutException;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * created by julian on 25/11/2022
 */
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
