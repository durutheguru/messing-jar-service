package com.julianduru.messingjarservice.entities;

import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotEmpty;

/**
 * created by julian on 23/01/2023
 */
@Data
@Document
public class Group extends BaseEntity {


    @NotEmpty(message = "Group name should not be empty")
    private String name;


    private String iconImageRef;


}

