package com.julianduru.messingjarservice.modules.group.gql;

import com.julianduru.messingjarservice.modules.group.GroupService;
import com.julianduru.messingjarservice.modules.group.dto.GroupDetailsDto;
import com.julianduru.messingjarservice.modules.group.dto.GroupPreviewDto;
import com.julianduru.messingjarservice.modules.group.dto.GroupUserPreviewDto;
import lombok.RequiredArgsConstructor;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;

import java.security.Principal;
import java.util.List;

/**
 * created by julian on 14/02/2023
 */
@Controller
@RequiredArgsConstructor
public class GroupQueryResolver {


    private final GroupService groupService;


    @QueryMapping
    public List<GroupPreviewDto> fetchGroupPreviews(
        @AuthenticationPrincipal Principal principal, @Argument int page, @Argument int size
    ) throws Exception {
        return groupService.fetchGroupPreviews(principal.getName(), page, size);
    }


    @QueryMapping
    public GroupDetailsDto fetchGroupDetails(
        @Argument String groupId
    ) throws Exception {
        return groupService.fetchGroupDetails(groupId);
    }


    @QueryMapping
    public List<GroupUserPreviewDto> fetchGroupMembers(
        @Argument String groupId
    ) throws Exception {
        return groupService.fetchGroupUsers(groupId).collectList().toFuture().join();
    }


}
