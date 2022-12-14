package com.julianduru.messingjarservice.modules.upload;

import com.julianduru.fileuploader.api.FileUpload;
import com.julianduru.fileuploader.repositories.FileUploadRepository;
import com.julianduru.messingjarservice.repositories.MessingJarFileUploadRepository;
import com.julianduru.messingjarservice.util.ReactiveBlocker;
import com.julianduru.messingjarservice.util.ReactiveListBlocker;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * created by julian on 20/11/2022
 */
@Component
@Primary
@RequiredArgsConstructor
public class AppFileUploadRepository implements FileUploadRepository {


    private final MessingJarFileUploadRepository messingJarFileUploadRepository;


    @Override
    public FileUpload save(FileUpload upload) {
        var savedUpload = new ReactiveBlocker<>(
            messingJarFileUploadRepository.save(
                com.julianduru.messingjarservice.entities.FileUpload.from(upload)
            )
        ).getValue();

        if (savedUpload != null) {
            return savedUpload.toApi();
        }

        throw new IllegalStateException("Failed to Save File Upload");
    }


    @Override
    public Page<FileUpload> findAll(Pageable pageable) {
        return new PageImpl<>(
            Objects.requireNonNull(
                messingJarFileUploadRepository.findAll()
                .map(com.julianduru.messingjarservice.entities.FileUpload::toApi)
                .collectList().block()
            )
        );
    }


    @Override
    public Optional<FileUpload> findByReference(String reference) {
        return Optional.ofNullable(
            new ReactiveBlocker<>(
                messingJarFileUploadRepository.findByReference(reference)
                    .map(com.julianduru.messingjarservice.entities.FileUpload::toApi)
            ).getValue()
        );
    }


    @Override
    public boolean existsByReference(String reference) {
        return messingJarFileUploadRepository.existsByReference(reference);
    }


    @Override
    public List<FileUpload> findByReferenceIn(Collection<String> references) {
        return new ReactiveListBlocker<>(messingJarFileUploadRepository
            .findByReferenceIn(references)
            .map(com.julianduru.messingjarservice.entities.FileUpload::toApi)
        ).getValue();
    }


}
