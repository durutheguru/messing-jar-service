package com.julianduru.messingjarservice.data;


import com.julianduru.util.test.DataProvider;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Arrays;

public interface MongoDataProvider<T> extends DataProvider<T> {



    ReactiveMongoRepository<T, String> getRepository();


    default Mono<T> save() {
        return this.getRepository().save(this.provide());
    }


    default Mono<T> save(T sample) {
        return this.getRepository().save(this.provide(sample));
    }


    default Flux<T> save(int count) {
        var flux = Flux.<T>just();

        for(int i = 0; i < count; ++i) {
            flux = flux.mergeWith(this.save());
        }

        return flux;
    }


    default Flux<T> save(T sample, int count) {
        var flux = Flux.<T>just();

        for(int i = 0; i < count; ++i) {
            flux.mergeWith(this.save(sample));
        }

        return flux;
    }


    default Flux<T> save(T... samples) {
        var repository = this.getRepository();
        var flux = Flux.<T>just();

        Arrays.stream(samples).forEach(sample -> {
            var savedSample = repository.save(this.provide(sample));
            flux.mergeWith(savedSample);
        });

        return flux;
    }


}


