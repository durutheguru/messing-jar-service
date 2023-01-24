package com.julianduru.messingjarservice.entities;

import lombok.Data;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DocumentReference;

/**
 * created by julian on 20/01/2023
 */
@Data
@Document
public class Chat extends BaseEntity {


    @DocumentReference
    private User user1;


    @DocumentReference
    private User user2;


}
