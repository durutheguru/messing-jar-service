package com.julianduru.messingjarservice.entities;

import lombok.Data;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * created by julian on 21/01/2023
 */
@Data
@Document
public class GroupUser extends BaseEntity {


    private ObjectId groupId;


    private ObjectId userId;


}

