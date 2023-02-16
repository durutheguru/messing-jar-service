package com.julianduru.messingjarservice.modules.upload.controller;

import com.julianduru.fileuploader.Upload;
import com.julianduru.fileuploader.UploadRequest;
import com.julianduru.fileuploader.api.FileData;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.awaitility.Awaitility;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.http.MediaType;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import reactor.core.scheduler.Schedulers;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.security.Principal;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * created by julian on 23/11/2022
 */
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(FileUploadController.PATH)
public class FileUploadController {

    public static final String PATH = "/file__upload";


    private final Upload upload;


    @Value("${file.uploader.container-name}")
    private String uploadContainerName;


    @Value("${file.uploader.file-key-prefix}")
    private String uploadFileKeyPrefix;



    @PostMapping( consumes = { MediaType.MULTIPART_FORM_DATA_VALUE } )
    @ResponseBody
    public FileData uploadFile(@AuthenticationPrincipal Principal principal, @RequestPart("file") FilePart filePart) throws IOException {
        var fileRead = new AtomicBoolean(false);
        var bout = new ByteArrayOutputStream();
        filePart.content()
            .map(
                buffer -> {
                    byte[] bytes = new byte[buffer.readableByteCount()];
                    buffer.read(bytes);
                    DataBufferUtils.release(buffer);

                    bout.writeBytes(bytes);
                    return new String(bytes);
                }
            )
            .doOnComplete(() -> fileRead.set(true))
            .subscribe();

        Awaitility.await().untilTrue(fileRead);

        return upload.uploadFile(
            UploadRequest.builder()
                .containerName(uploadContainerName)
                .fileKey(uploadFileKeyPrefix + filePart.filename())
                .fileName(filePart.filename())
                .fileType(Objects.requireNonNull(filePart.headers().getContentType()).getType())
                .inputStream(new ByteArrayInputStream(bout.toByteArray()))
                .build()
        ).data();
    }


}
