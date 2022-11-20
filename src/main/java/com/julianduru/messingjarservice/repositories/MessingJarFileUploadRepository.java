package com.julianduru.messingjarservice.repositories;

import com.julianduru.messingjarservice.entities.FileUpload;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Collection;

/**
 * created by julian on 20/11/2022
 */
@Repository
public interface MessingJarFileUploadRepository extends BaseEntityRepository<FileUpload> {


    Mono<FileUpload> findByReference(String reference);


    default boolean existsByReference(String reference) {
        return countByReference(reference).block() > 0;
    }


    Mono<Long> countByReference(String reference);


    Flux<FileUpload> findByReferenceIn(Collection<String> references);


}
