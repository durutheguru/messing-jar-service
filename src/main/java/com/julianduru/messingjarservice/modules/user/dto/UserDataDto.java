package com.julianduru.messingjarservice.modules.user.dto;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

/**
 * created by julian on 30/10/2022
 */
@Data
public class UserDataDto {


    private String userId;


    @NotEmpty(message = "Username is required")
    private String username;


    @NotEmpty(message = "First Name is required")
    @Size(max = 50, message = "First Name should not exceed {max}")
    private String firstName;


    @NotEmpty(message = "Last Name is required")
    @Size(max = 50, message = "Last Name should not exceed {max}")
    private String lastName;


    @NotEmpty(message = "Email is required")
    @Size(max = 100, message = "Email should not exceed {max}")
    private String email;


    private String profilePhotoRef;


    private String profilePhotoPublicUrl;


    private boolean enableEmails;


    public String getFullName() {
        return firstName + " " + lastName;
    }


}
