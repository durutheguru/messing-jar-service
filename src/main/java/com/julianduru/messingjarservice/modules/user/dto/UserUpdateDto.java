package com.julianduru.messingjarservice.modules.user.dto;

import lombok.Data;

/**
 * created by julian on 30/10/2022
 */
@Data
public class UserUpdateDto {


    private String firstName;


    private String lastName;


    private String email;


    private String profilePhotoRef;


    private boolean enableEmails;


}
