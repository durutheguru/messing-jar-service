package com.julianduru.messingjarservice.entities;

import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * created by julian on 01/11/2022
 */
@Data
@Document
public class Settings extends BaseEntity {


    private String username;


    private boolean enableEmails;


}
