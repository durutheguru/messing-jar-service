package com.julianduru.messingjarservice.entities;

import lombok.Data;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.ZonedDateTime;

/**
 * created by julian on 23/01/2023
 */
@Data
@Document
public class Group extends BaseEntity {


    @NotEmpty(message = "Group name should not be empty")
    private String name;


    private String iconImageRef;


    @NotNull(message = "Group owner should not be empty")
    private ObjectId ownerUserId;


    private ZonedDateTime lastMessageTimestamp;


    public Group ownerUserId(ObjectId ownerUserId) {
        this.ownerUserId = ownerUserId;
        return this;
    }


}

