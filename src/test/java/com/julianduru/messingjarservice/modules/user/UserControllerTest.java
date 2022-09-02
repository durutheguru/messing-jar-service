package com.julianduru.messingjarservice.modules.user;

import com.julianduru.messingjarservice.data.UserDtoProvider;
import com.julianduru.messingjarservice.repositories.UserRepository;
import com.julianduru.util.JSONUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import reactor.test.StepVerifier;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

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
//        mockMvc.perform(
//            post(UserController.PATH)
//                .contentType(MediaType.APPLICATION_JSON)
//                .content(JSONUtil.asJsonString(userDtoProvider.provide()))
//        ).andDo(print())
//            .andExpect(status().is2xxSuccessful());


        var userDto = userDtoProvider.provide();

        webTestClient
            .post()
            .uri(UserController.PATH)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(JSONUtil.asJsonString(userDto))
            .exchange()
            .expectStatus().is2xxSuccessful();

        StepVerifier
            .create(userRepository.findByUsername(userDto.getUsername()))
            .expectNextMatches(u -> u.getUsername().equalsIgnoreCase(userDto.getUsername()))
            .verifyComplete();
    }


}

