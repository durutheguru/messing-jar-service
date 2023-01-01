package com.julianduru.messingjarservice.entities;

import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotEmpty;

/**
 * created by julian on 01/11/2022
 */
@Data
@Document
public class Settings extends BaseEntity {


    @NotEmpty(message = "Settings username should not be empty")
    private String username;


    private boolean enableEmails;


}
