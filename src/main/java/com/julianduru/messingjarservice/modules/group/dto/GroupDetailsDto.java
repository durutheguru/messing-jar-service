package com.julianduru.messingjarservice.modules.group.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * created by julian on 20/02/2023
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GroupDetailsDto {


    private String id;


    private String name;


    private int memberCount;


}
