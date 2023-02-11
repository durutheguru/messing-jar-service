package com.julianduru.messingjarservice.modules.group;

import com.julianduru.messingjarservice.dto.GroupDto;
import com.julianduru.messingjarservice.dto.GroupUserDto;
import com.julianduru.messingjarservice.entities.Group;
import com.julianduru.messingjarservice.entities.GroupUser;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * created by julian on 10/02/2023
 */
public interface GroupService {


    Mono<Group> saveGroup(GroupDto groupDto);


    Mono<GroupUser> addGroupUser(GroupUserDto groupUserDto);


    Flux<GroupUser> findGroupUsers(String groupId);


}
