package com.julianduru.messingjarservice.modules.group;

import com.julianduru.messingjarservice.data.GroupDtoProvider;
import com.julianduru.messingjarservice.data.GroupUserDtoProvider;
import com.julianduru.messingjarservice.modules.BaseControllerTest;
import com.julianduru.util.JSONUtil;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * created by julian on 10/02/2023
 */
public class GroupControllerTest extends BaseControllerTest {


    @Autowired
    private GroupDtoProvider groupDtoProvider;


    @Autowired
    private GroupRepository groupRepository;


    @Autowired
    private GroupUserDtoProvider groupUserDtoProvider;


    @Autowired
    private GroupUserRepository groupUserRepository;


    @Test
    public void saveNewGroup() throws Exception {
        var groupDto = groupDtoProvider.provide();

        webTestClient
            .post()
            .uri(GroupController.PATH)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(JSONUtil.asJsonString(groupDto))
            .exchange()
            .expectStatus().is2xxSuccessful()
            .expectBody()
            .consumeWith(System.out::println);
    }


    @Test
    public void addUserToGroup() throws Exception {
        var groupUserDto = groupUserDtoProvider.provide();

        webTestClient
            .post()
            .uri(GroupController.PATH + "/user")
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(JSONUtil.asJsonString(groupUserDto))
            .exchange()
            .expectStatus().is2xxSuccessful()
            .expectBody()
            .consumeWith(System.out::println);

        var groupUser = groupUserRepository.findByGroupIdAndUserId(
            new ObjectId(groupUserDto.getGroupId()),
            new ObjectId(groupUserDto.getUserId())
        ).toFuture().get();
        assertThat(groupUser).isNotNull();
    }


}


