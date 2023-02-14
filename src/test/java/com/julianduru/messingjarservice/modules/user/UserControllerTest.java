package com.julianduru.messingjarservice.modules.user;

import com.julianduru.messingjarservice.data.dto.UserDataUpdateProvider;
import com.julianduru.messingjarservice.data.dto.UserDtoProvider;
import com.julianduru.messingjarservice.modules.BaseControllerTest;
import com.julianduru.util.JSONUtil;
import graphql.kickstart.spring.webclient.boot.GraphQLRequest;
import graphql.kickstart.spring.webclient.boot.GraphQLWebClient;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.jdbc.JdbcTestUtils;
import reactor.test.StepVerifier;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * created by julian on 28/08/2022
 */
public class UserControllerTest extends BaseControllerTest {


    @Autowired
    private UserDtoProvider userDtoProvider;


    @Autowired
    private UserRepository userRepository;


    @Autowired
    private UserDataUpdateProvider userDataUpdateProvider;


    @Autowired
    private GraphQLWebClient oauthServerGraphQLClient;


    @Autowired
    private JdbcTemplate oauthServiceJdbcTemplate;


    @Autowired
    private SettingsRepository settingsRepository;



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


    /**
     * TODO:
     * - debug docker-compose
     * - test with embedded mongo
     * @throws Exception
     */
    @Test
    public void testUpdatingUserSettings() throws Exception {
        // save user on oauth-service
        saveUser();

        var userUpdate = userDataUpdateProvider.provide();


        // post user data update to messing-jar-service
        webTestClient
            .patch()
            .uri(UserController.PATH)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(JSONUtil.asJsonString(userUpdate))
            .exchange()
            .expectStatus().is2xxSuccessful()
            .expectBody()
            .consumeWith(System.out::println);


        StepVerifier.create(
            settingsRepository
            .findByUsername("messing-jar-service")
        ).expectNextMatches(
            s -> {
                assertThat(s.isEnableEmails()).isTrue();
                return true;
            }
        ).verifyComplete();


        // ensure updates are reflected on oauth-service db
        var countUpdatedRows = JdbcTestUtils.countRowsInTableWhere(
            oauthServiceJdbcTemplate,
            "user_data",
            String.format(
                "first_name = '%s' AND last_name = '%s' AND email = '%s'",
                userUpdate.getFirstName(), userUpdate.getLastName(), userUpdate.getEmail()
            )
        );
        assertThat(countUpdatedRows).isEqualTo(1);
    }

    private void saveUser() {
        var response = oauthServerGraphQLClient.post(
            GraphQLRequest.builder()
                .query(
                    """
                        mutation SaveUser(
                          $username: String!
                          $password: String!
                          $firstName: String!
                          $lastName: String!
                          $email: String!
                        ) {
                            saveUser(userDto: {
                                username: $username,
                                password: $password,
                                firstName: $firstName,
                                lastName: $lastName, 
                                email: $email 
                            }) {
                                username
                                firstName
                                lastName
                            }
                        }
                        """
                )
                .variables(
                    Map.of(
                        "username", "messing-jar-service",
                        "password", faker.code().isbn10(),
                        "firstName", faker.name().firstName(),
                        "lastName", faker.name().lastName(),
                        "email", faker.internet().emailAddress()
                    )
                )
                .build()
        ).blockOptional();

        var gqlResponse = response.get();
        var errorOccurred = !gqlResponse.getErrors().isEmpty();

        if (errorOccurred) {
            var error = gqlResponse.getErrors().get(0);
            assertThat(error.getMessage().contains("already exists")).isTrue();
        }
    }


}

