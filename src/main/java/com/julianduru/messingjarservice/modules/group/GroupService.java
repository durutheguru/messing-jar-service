package com.julianduru.messingjarservice.modules.group;

import com.julianduru.messingjarservice.modules.group.dto.*;
import com.julianduru.messingjarservice.entities.Group;
import com.julianduru.messingjarservice.entities.GroupUser;
import org.bson.types.ObjectId;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.security.Principal;
import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * created by julian on 10/02/2023
 */
public interface GroupService {


    Mono<Group> saveGroup(Principal principal, GroupDto groupDto) throws ExecutionException, InterruptedException;


    Flux<Group> findGroups();


    Mono<GroupUser> addGroupUser(GroupUserDto groupUserDto);


    Flux<GroupUser> findGroupUsers(String groupId);


    Flux<Group> findUserGroups(ObjectId userId);


    List<GroupPreviewDto> fetchGroupPreviews(String username, int page, int size) throws ExecutionException, InterruptedException;


    GroupDetailsDto fetchGroupDetails(String groupId) throws ExecutionException, InterruptedException;


    Flux<GroupUserPreviewDto> fetchGroupUsers(String groupId);


}