package com.julianduru.messingjarservice.modules.upload;

import com.julianduru.messingjarservice.entities.FileUpload;
import com.julianduru.messingjarservice.BaseEntityRepository;
import org.awaitility.Awaitility;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Collection;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * created by julian on 20/11/2022
 */
@Repository
public interface MessingJarFileUploadRepository extends BaseEntityRepository<FileUpload> {


    Mono<FileUpload> findByReference(String reference);


    default boolean existsByReference(String reference) {
        var value = new boolean[1];
        var itemSet = new AtomicBoolean(false);
        var mono = countByReference(reference).map(r -> r  > 0);

        mono.subscribe(b -> {
            value[0] = b;
            itemSet.set(true);
        });

        Awaitility.await().untilTrue(itemSet);

        return value[0];
    }


    Mono<Long> countByReference(String reference);


    Flux<FileUpload> findByReferenceIn(Collection<String> references);


}
