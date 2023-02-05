package com.julianduru.messingjarservice.graphql.chats;

import com.julianduru.messingjarservice.modules.chat.dto.ChatPreviewDto;
//import graphql.kickstart.tools.GraphQLQueryResolver;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;


/**
 * created by julian on 05/02/2023
 */
@Component
@RequiredArgsConstructor
public class ChatQueryResolver {




    public List<ChatPreviewDto> fetchChatPreviews(int page, int size) {

        return null;
    }


}
