package com.julianduru.messingjarservice.modules.group.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * created by julian on 14/02/2023
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GroupPreviewDto {


    private String groupName;

    private String lastMessage;

    private String lastMessageTimeStamp;

    private String groupImageUrl;


}
