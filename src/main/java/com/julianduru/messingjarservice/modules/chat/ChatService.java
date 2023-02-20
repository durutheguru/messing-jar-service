package com.julianduru.messingjarservice.modules.chat;

import com.julianduru.messingjarservice.modules.chat.dto.ChatPreviewDto;

import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * created by julian on 05/02/2023
 */
public interface ChatService {


    List<ChatPreviewDto> fetchChatPreviews(String username, int page, int size) throws ExecutionException, InterruptedException;


}
