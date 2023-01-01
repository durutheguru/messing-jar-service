package com.julianduru.messingjarservice.entities;

import lombok.Data;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * created by julian on 27/08/2022
 */
@Data
@Document
public class User extends BaseEntity {


    @Indexed(unique = true)
    private String username;

    public User() {}

    public User(String username) {
        this.username = username;
    }

}
