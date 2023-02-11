package com.julianduru.messingjarservice.modules.group;

import com.julianduru.messingjarservice.dto.GroupDto;
import com.julianduru.messingjarservice.dto.GroupUserDto;
import com.julianduru.messingjarservice.entities.Group;
import com.julianduru.messingjarservice.entities.GroupUser;
import com.julianduru.messingjarservice.repositories.GroupRepository;
import com.julianduru.messingjarservice.repositories.GroupUserRepository;
import com.julianduru.messingjarservice.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * created by julian on 10/02/2023
 */
@Service
@RequiredArgsConstructor
public class GroupServiceImpl implements GroupService {


    private final GroupRepository groupRepository;


    private final UserRepository userRepository;


    private final GroupUserRepository groupUserRepository;



    @Override
    public Mono<Group> saveGroup(GroupDto groupDto) {
        return groupRepository.save(groupDto.toEntity());
    }


    @Override
    public Mono<GroupUser> addGroupUser(GroupUserDto groupUserDto) {
        var userMono = userRepository.findById(new ObjectId(groupUserDto.getUserId()));
        var groupMono = groupRepository.findById(new ObjectId(groupUserDto.getGroupId()));

        return Mono
            .zip(userMono, groupMono)
            .flatMap(
                data -> {
                    var user = data.getT1();
                    var group = data.getT2();

                    var groupUser = new GroupUser();

                    groupUser.setUserId(user.getId());
                    groupUser.setGroupId(group.getId());

                    return groupUserRepository.save(groupUser);
                }
            )
            .onErrorResume(Mono::error);
    }


    @Override
    public Flux<GroupUser> findGroupUsers(String groupId) {
        return null;
    }


}


