package com.julianduru.messingjarservice.util;

import org.awaitility.Awaitility;
import reactor.core.publisher.Mono;

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
        monoPublisher
            .map(t -> {
                value = t;
                valueSet.set(true);

                return value;
            })
            .subscribe();

        Awaitility.await().untilTrue(valueSet);

        return value;
    }


}
