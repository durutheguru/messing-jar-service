package com.julianduru.messingjarservice.modules.upload.controller;

import com.julianduru.messingjarservice.modules.BaseControllerTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.client.MultipartBodyBuilder;
import org.springframework.web.reactive.function.BodyInserters;

/**
 * created by julian on 13/12/2022
 */
public class FileUploadControllerTest extends BaseControllerTest {


    @Value("classpath:files/photo.jpg")
    Resource uploadResource;



    @Test
    public void testUploadingFile() throws Exception {
        var multipartBodyBuilder = new MultipartBodyBuilder();
        multipartBodyBuilder
            .part("file", uploadResource)
            .contentType(MediaType.MULTIPART_FORM_DATA);


        webTestClient
            .post()
            .uri(FileUploadController.PATH)
            .body(BodyInserters.fromMultipartData(multipartBodyBuilder.build()))
            .exchange()
            .expectStatus()
            .isOk();

    }


}

