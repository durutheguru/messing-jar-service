package com.julianduru.messingjarservice.modules.chat.gql;

import com.julianduru.messingjarservice.BaseIntegrationTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.test.tester.AbstractDelegatingGraphQlTester;
import org.springframework.graphql.test.tester.HttpGraphQlTester;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.test.web.reactive.server.WebTestClientConfigurer;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * created by julian on 05/02/2023
 */

public class ChatQueryResolverTest extends BaseIntegrationTest {

    @Autowired
    private WebTestClient webTestClient;

    @Autowired
    private HttpGraphQlTester graphQlTester;


    @Test
    @WithMockUser(username = "remilekun")
    public void fetchChatPreviews() throws Exception {
        var response = graphQlTester
            .mutate()
            .build()
            .document(
                """
                {
                    fetchChatPreviews {
                        chatId
                        fullName
                        lastMessage
                        lastMessageTimeStamp
                        profilePictureUrl
                    }
                }
                """
            ).execute();

        response.path("$.data.fetchChatPreviews").hasValue();
    }


}

