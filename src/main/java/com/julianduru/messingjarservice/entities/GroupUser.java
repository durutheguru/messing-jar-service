package com.julianduru.messingjarservice.entities;

import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DocumentReference;

/**
 * created by julian on 21/01/2023
 */
@Data
@Document
public class GroupUser extends BaseEntity {


    @DocumentReference
    private Group group;


    @DocumentReference
    private User user;


}
