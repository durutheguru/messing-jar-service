package com.julianduru.messingjarservice.entities;

import lombok.Data;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * created by julian on 20/01/2023
 */
@Data
@Document
public class Chat extends BaseEntity {


    private ObjectId user1;


    private ObjectId user2;


}
