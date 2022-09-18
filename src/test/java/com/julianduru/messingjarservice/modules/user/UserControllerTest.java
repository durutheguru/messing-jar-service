package com.julianduru.messingjarservice.modules.user;

import com.julianduru.messingjarservice.data.UserDtoProvider;
import com.julianduru.messingjarservice.modules.BaseControllerTest;
import com.julianduru.messingjarservice.repositories.UserRepository;
import com.julianduru.util.JSONUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import reactor.test.StepVerifier;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * created by julian on 28/08/2022
 */
public class UserControllerTest extends BaseControllerTest {


    @Autowired
    private UserDtoProvider userDtoProvider;


    @Autowired
    private UserRepository userRepository;



    @Test
    public void testSavingNewUser() throws Exception {
        var userDto = userDtoProvider.provide();

        webTestClient
            .post()
            .uri(UserController.PATH)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(JSONUtil.asJsonString(userDto))
            .exchange()
            .expectStatus().is2xxSuccessful()
            .expectBody()
            .consumeWith(System.out::println);

        StepVerifier
            .create(userRepository.findByUsername(userDto.getUsername()))
            .expectNextMatches(u -> {
                assertThat(u.getUsername()).isEqualTo(userDto.getUsername());
                return true;
            })
            .verifyComplete();
    }


}

