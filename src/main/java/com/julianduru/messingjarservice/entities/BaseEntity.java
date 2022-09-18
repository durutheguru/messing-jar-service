package com.julianduru.messingjarservice.entities;

import lombok.Data;
import org.springframework.data.annotation.*;
import org.springframework.data.domain.Persistable;
import org.springframework.data.mongodb.core.index.Indexed;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;

/**
 * created by julian on 27/08/2022
 */
@Data
public class BaseEntity implements Persistable<String> {


    @Id
    private String id;


    @Indexed(unique = true)
    private String code;


    @CreatedDate
    private LocalDateTime createdDate;


    @CreatedBy
    private String createdBy;


    @LastModifiedDate
    private LocalDateTime lastModifiedDate;


    @LastModifiedBy
    private String lastModifiedBy;


    @Override
    public boolean isNew() {
        return id == null;
    }


}
