package com.julianduru.messingjarservice.modules.search.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * created by julian on 26/12/2022
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserSearchResult {

    
    private String userId;


    private String username;


    private String firstName;


    private String lastName;


    private String email;


    private String profilePhotoRef;


    private String profilePhotoUrl;


}

