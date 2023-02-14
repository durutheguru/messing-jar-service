package com.julianduru.messingjarservice.modules.group;

import com.julianduru.messingjarservice.ServiceConstants;
import com.julianduru.messingjarservice.modules.group.dto.GroupDto;
import com.julianduru.messingjarservice.modules.group.dto.GroupUserDto;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import javax.validation.Valid;
import java.util.concurrent.ExecutionException;

/**
 * created by julian on 10/02/2023
 */
@RestController
@RequiredArgsConstructor
@RequestMapping(GroupController.PATH)
public class GroupController {


    public static final String PATH = ServiceConstants.API_BASE + "/group";


    private final GroupService groupService;


    @PostMapping
    public Mono<GroupDto> saveGroup(@Valid @RequestBody GroupDto groupDto) throws ExecutionException, InterruptedException {
        return groupService.saveGroup(groupDto).map(GroupDto::fromEntity);
    }


    @PostMapping("/user")
    public Mono<GroupUserDto> addUserToGroup(@Valid @RequestBody GroupUserDto groupUserDto) {
        return groupService.addGroupUser(groupUserDto).map(GroupUserDto::fromEntity);
    }


}

