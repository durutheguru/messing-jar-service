package com.julianduru.messingjarservice.modules.user.dto;

import lombok.Data;

import java.util.List;
import java.util.Map;


@Data
public class OAuthUserData {


    private Long id;


    private String username;


    private String firstName;


    private String lastName;


    private String email;


    private List<String> authorities;


    private Map<String, String> additionalInfo;


    private boolean locked;


    private boolean credentialsExpired;


}

