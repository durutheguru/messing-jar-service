package com.julianduru.messingjarservice.modules.chat.dto;

import com.julianduru.messingjarservice.modules.user.dto.UserDataDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * created by julian on 28/01/2023
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChatInitializationResponseDto {


    private UserDataDto initiatorDetails;

    private UserDataDto receiverDetails;

    private List<ChatMessageDto> history;


}
