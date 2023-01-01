package com.julianduru.messingjarservice.entities;

import com.julianduru.fileuploader.UploadRequest;
import com.julianduru.fileuploader.providers.UploadProvider;
import lombok.Data;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.util.StringUtils;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

/**
 * created by julian on 20/11/2022
 */
@Data
@Document
public class FileUpload extends BaseEntity {


    @NotNull(message = "Provider should not be null")
    private UploadProvider provider;


    @NotEmpty(message = "Container Name is required")
    private String containerName;


    @NotEmpty(message = "File Key is required")
    private String fileKey;


    @NotEmpty(message = "Original File Name is required")
    private String originalFileName;


    @NotEmpty(message = "File Type is required")
    private String fileType;


    @Indexed(unique = true)
    @NotEmpty(message = "Reference is required")
    private String reference;


    private String publicUrl;


    private String metaData;




    public static FileUpload fromRequest(UploadRequest uploadRequest, UploadProvider defaultUploadProvider) {
        var upload = new FileUpload();

        upload.setContainerName(uploadRequest.getContainerName());
        upload.setFileKey(uploadRequest.getFileKey());
        upload.setReference(uploadRequest.getReference());
        upload.setMetaData(uploadRequest.getMetaData());
        upload.setOriginalFileName(uploadRequest.getFileName());
        upload.setFileType(uploadRequest.getFileType());

        if (uploadRequest.providerless()) {
            upload.setProvider(defaultUploadProvider);
        }
        else {
            upload.setProvider(uploadRequest.getProvider());
        }

        return upload;
    }


    public static FileUpload from(com.julianduru.fileuploader.api.FileUpload upload) {
        var fileUpload = new FileUpload();

        fileUpload.setContainerName(upload.getContainerName());
        fileUpload.setFileKey(upload.getFileKey());
        fileUpload.setReference(upload.getReference());
        fileUpload.setMetaData(upload.getMetaData());
        fileUpload.setOriginalFileName(upload.getOriginalFileName());
        fileUpload.setFileType(upload.getFileType());
        fileUpload.setProvider(upload.getProvider());
        fileUpload.setId(StringUtils.hasText(upload.getId()) ? String.valueOf(upload.getId()) : null);
        fileUpload.setPublicUrl(upload.getPublicUrl());

        return fileUpload;
    }


    public com.julianduru.fileuploader.api.FileUpload toApi() {
        var fileUpload = new com.julianduru.fileuploader.api.FileUpload();

        fileUpload.setContainerName(getContainerName());
        fileUpload.setFileKey(getFileKey());
        fileUpload.setReference(getReference());
        fileUpload.setMetaData(getMetaData());
        fileUpload.setOriginalFileName(getOriginalFileName());
        fileUpload.setFileType(getFileType());
        fileUpload.setProvider(getProvider());
        fileUpload.setId(getId());
        fileUpload.setPublicUrl(getPublicUrl());

        return fileUpload;
    }


}
