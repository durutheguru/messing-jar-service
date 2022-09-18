package com.julianduru.messingjarservice.modules;

import com.julianduru.messingjarservice.BaseIntegrationTest;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.codec.ClientCodecConfigurer;
import org.springframework.security.oauth2.client.ReactiveOAuth2AuthorizedClientManager;
import org.springframework.security.oauth2.client.web.reactive.function.client.ServerOAuth2AuthorizedClientExchangeFilterFunction;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.client.ExchangeStrategies;

/**
 * created by julian on 28/08/2022
 */
@Slf4j
public class BaseControllerTest extends BaseIntegrationTest {


    @Autowired
    protected WebTestClient webTestClient;


    @Autowired
    private ReactiveOAuth2AuthorizedClientManager manager;


    private boolean webTestClientInitialized = false;



    @BeforeEach
    public void beforeClass() {
        initializeWebTestClient();
    }


    private void initializeWebTestClient() {
        if (webTestClientInitialized) {
            log.info("Web Test Client already initialized");
            return;
        }

        var exchangeStrategies =
            ExchangeStrategies.builder()
                .codecs(ClientCodecConfigurer::defaultCodecs)
                .build();
        var oauth = new ServerOAuth2AuthorizedClientExchangeFilterFunction(manager);
        oauth.setDefaultClientRegistrationId("messing-jar-service");

        webTestClient = webTestClient.mutate()
            .filter(oauth)
            .exchangeStrategies(exchangeStrategies)
            .build();

        webTestClientInitialized = true;
    }


}



