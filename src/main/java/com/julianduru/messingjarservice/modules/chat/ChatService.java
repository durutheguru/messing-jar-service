package com.julianduru.messingjarservice.modules.chat;

import com.julianduru.messingjarservice.modules.chat.dto.ChatPreviewDto;

import java.util.List;

/**
 * created by julian on 05/02/2023
 */
public interface ChatService {


    List<ChatPreviewDto> fetchChatPreviews(int page, int size);


}
