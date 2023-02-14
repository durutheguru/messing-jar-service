package com.julianduru.messingjarservice.modules.group.gql;

import com.julianduru.messingjarservice.modules.group.GroupService;
import com.julianduru.messingjarservice.modules.group.dto.GroupPreviewDto;
import lombok.RequiredArgsConstructor;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import java.util.List;

/**
 * created by julian on 14/02/2023
 */
@Controller
@RequiredArgsConstructor
public class GroupQueryResolver {


    private final GroupService groupService;


    @QueryMapping
    public List<GroupPreviewDto> fetchGroupPreviews(@Argument int page, @Argument int size) throws Exception {
        return groupService.fetchGroupPreviews(page, size);
    }


}
