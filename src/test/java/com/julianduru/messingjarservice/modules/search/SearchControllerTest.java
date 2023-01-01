package com.julianduru.messingjarservice.modules.search;

import com.julianduru.messingjarservice.modules.BaseControllerTest;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

/**
 * created by julian on 26/12/2022
 */
public class SearchControllerTest extends BaseControllerTest {


    @Test
    public void searchingByQuery() throws Exception {
        webTestClient.get()
            .uri(builder -> builder.path(SearchController.PATH)
                .queryParam("query", "remi")
                .build()
            )
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus().is2xxSuccessful()
            .expectBody()
            .consumeWith(System.out::println);
    }


}

