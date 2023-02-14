package com.julianduru.messingjarservice.modules.chat.service;

import com.julianduru.messingjarservice.BaseIntegrationTest;
import com.julianduru.messingjarservice.modules.chat.ChatService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * created by julian on 05/02/2023
 */
public class FetchChatPreviewsTest extends BaseIntegrationTest {


    @Autowired
    private ChatService chatService;



    @Test
    public void fetchChatPreviews() throws Exception {
        var previews = chatService.fetchChatPreviews(0, 5);
        assertThat(previews).isNotEmpty();
    }


}
