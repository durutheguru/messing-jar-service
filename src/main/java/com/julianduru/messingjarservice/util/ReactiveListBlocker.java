package com.julianduru.messingjarservice.util;

import org.awaitility.Awaitility;
import reactor.core.publisher.Flux;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * created by julian on 26/11/2022
 */
public class ReactiveListBlocker<T> {


    private List<T> value;

    private AtomicBoolean valuesSet;

    private Flux<T> fluxPublisher;


    public ReactiveListBlocker(Flux<T> publisher) {
        valuesSet = new AtomicBoolean(false);

        value = new ArrayList<>();
        this.fluxPublisher = publisher;
    }


    public List<T> getValue() {
        fluxPublisher
            .map(t -> {
                value.add(t);
                return value;
            })
            .doOnComplete(() -> valuesSet.set(true))
            .subscribe(i -> System.out.println("Item: " + i), e -> {
                System.err.println("Error..." + e.getMessage());
                e.printStackTrace();
            });

        Awaitility.await().untilTrue(valuesSet);

        return value;
    }


}
