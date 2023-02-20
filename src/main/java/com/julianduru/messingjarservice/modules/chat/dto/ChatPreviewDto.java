package com.julianduru.messingjarservice.modules.chat.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * created by julian on 05/02/2023
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChatPreviewDto {

    private String chatId;

    private String username;

    private String fullName;

    private String lastMessage;

    private String lastMessageTimeStamp;

    private String profilePictureUrl;

}
