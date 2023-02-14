package com.julianduru.messingjarservice.modules.group.gql;

import com.julianduru.messingjarservice.BaseIntegrationTest;
import com.julianduru.messingjarservice.data.entities.GroupMessageDataProvider;
import com.julianduru.messingjarservice.data.entities.GroupUserDataProvider;
import com.julianduru.messingjarservice.data.entities.UserDataProvider;
import com.julianduru.messingjarservice.entities.GroupMessage;
import com.julianduru.messingjarservice.entities.GroupUser;
import com.julianduru.messingjarservice.entities.User;
import com.julianduru.messingjarservice.modules.group.GroupRepository;
import com.julianduru.messingjarservice.modules.user.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.test.tester.HttpGraphQlTester;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.time.Duration;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * created by julian on 14/02/2023
 */
public class GroupQueryResolverTest extends BaseIntegrationTest {


    @Autowired
    private WebTestClient webTestClient;


    @Autowired
    private HttpGraphQlTester graphQlTester;


    @Autowired
    private UserDataProvider userDataProvider;


    @Autowired
    private GroupUserDataProvider groupUserDataProvider;


    @Autowired
    private GroupMessageDataProvider groupMessageDataProvider;


    @Autowired
    private GroupRepository groupRepository;


    @Test
    @WithMockUser(username = "remilekun")
    public void fetchChatPreviews() throws Exception {
        var user = ((UserRepository)userDataProvider.getRepository())
            .findByUsername("remilekun")
            .toFuture()
            .join();

        var groupUserSample = new GroupUser();
        groupUserSample.setUserId(user.getId());

        var groupUsers = groupUserDataProvider.save(groupUserSample, 7)
            .collectList()
            .toFuture().get();

        var groupIds = groupUsers.stream().map(GroupUser::getGroupId).toList();
        var persistedGroupUsers = groupIds.stream().map(
            groupId -> {
                var sample = new GroupUser();
                sample.setGroupId(groupId);
                return groupUserDataProvider.save(sample, 3)
                    .collectList()
                    .toFuture().join();
            }
        ).flatMap(List::stream).toList();

        for (var groupUser : persistedGroupUsers) {
            var messageSample = new GroupMessage();
            messageSample.setFromUserId(groupUser.getUserId());
            messageSample.setGroupId(groupUser.getGroupId());
            groupMessageDataProvider.save(messageSample).toFuture().join();;

            groupRepository.updateLastMessageTimeStampById(
                groupUser.getGroupId(), ZonedDateTime.now()
            ).toFuture().join();
        }

        var response = graphQlTester
            .mutate()
            .responseTimeout(Duration.ofSeconds(60))
            .build()
            .document(
                """
                {
                    fetchGroupPreviews {
                        groupName
                        lastMessage
                        lastMessageTimeStamp
                        groupImageUrl
                    }
                }
                """
            ).execute();

        response.path("$.data.fetchGroupPreviews").hasValue();
    }


}

