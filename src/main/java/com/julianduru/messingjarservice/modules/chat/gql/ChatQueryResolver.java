package com.julianduru.messingjarservice.modules.chat.gql;

import com.julianduru.messingjarservice.modules.chat.ChatService;
import com.julianduru.messingjarservice.modules.chat.dto.ChatPreviewDto;
import lombok.RequiredArgsConstructor;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import java.util.List;


/**
 * created by julian on 05/02/2023
 */
@Controller
@RequiredArgsConstructor
public class ChatQueryResolver {


    private final ChatService chatService;


    @QueryMapping
    public List<ChatPreviewDto> fetchChatPreviews(@Argument int page, @Argument int size) throws Exception {
        return chatService.fetchChatPreviews(page, size);
    }


}
