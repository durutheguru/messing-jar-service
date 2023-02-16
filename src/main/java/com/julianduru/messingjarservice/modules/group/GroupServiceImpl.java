package com.julianduru.messingjarservice.modules.group;

import com.julianduru.fileuploader.repositories.FileUploadRepository;
import com.julianduru.messingjarservice.modules.group.dto.GroupDto;
import com.julianduru.messingjarservice.modules.group.dto.GroupUserDto;
import com.julianduru.messingjarservice.entities.Group;
import com.julianduru.messingjarservice.entities.GroupUser;
import com.julianduru.messingjarservice.entities.User;
import com.julianduru.messingjarservice.modules.group.dto.GroupPreviewDto;
import com.julianduru.messingjarservice.modules.user.UserRepository;
import com.julianduru.messingjarservice.util.AuthUtil;
import com.julianduru.util.TimeUtil;
import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.security.Principal;
import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * created by julian on 10/02/2023
 */
@Service
@RequiredArgsConstructor
public class GroupServiceImpl implements GroupService {


    private final GroupRepository groupRepository;


    private final UserRepository userRepository;


    private final GroupUserRepository groupUserRepository;


    private final GroupMessageRepository groupMessageRepository;


    private final FileUploadRepository fileUploadRepository;



    @Override
    public Mono<Group> saveGroup(Principal principal, GroupDto groupDto) throws ExecutionException, InterruptedException {
        var username = principal.getName();

        return userRepository.findByUsername(username)
            .switchIfEmpty(
                Mono.error(
                    new RuntimeException(String.format("User %s not found", username))
                )
            )
            .flatMap(user -> groupRepository.save(
                groupDto.toEntity().ownerUserId(user.getId())
            ));
    }


    @Override
    public Flux<Group> findGroups() {
        return groupRepository.findAll(Sort.by(Sort.Direction.ASC, "name"));
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
        return groupUserRepository.findGroupUsersByGroupId(new ObjectId(groupId));
    }


    @Override
    public Flux<Group> findUserGroups(ObjectId userId) {
        // TODO: fix this bad code.
        return groupUserRepository.findGroupUsersByUserId(userId)
            .flatMap(groupUser -> groupRepository.findById(groupUser.getGroupId()));
    }


    @Override
    public List<GroupPreviewDto> fetchGroupPreviews(int page, int size) throws ExecutionException, InterruptedException {
        return AuthUtil.authUser(userRepository)
            .switchIfEmpty(
                Mono.error(new RuntimeException("User not found"))
            )
            .flux()
            .map(
                user -> {
                    try {
                        var groups = findUserGroups(user.getId()).collectList().toFuture().get()
                            .stream()
                            .sorted((g1, g2) -> g2.getLastMessageTimestamp() != null && g1.getLastMessageTimestamp() != null ?
                                g2.getLastMessageTimestamp().compareTo(g1.getLastMessageTimestamp()) : g2.getName().compareTo(g1.getName()))
                            .limit(5)
                            .toList();

                        var groupIds = groups.stream().map(Group::getId).toList();
                        return groupIds.stream()
                            .map(groupMessageRepository::findFirstByGroupIdOrderByCreatedDateDesc)
                            .map(
                                groupMessageMono -> {
                                    var groupMessage = groupMessageMono.toFuture().join();
                                    var group = groups.stream().filter(g -> g.getId().equals(groupMessage.getGroupId())).findFirst().get();
                                    var groupPreviewDto = new GroupPreviewDto();

                                    groupPreviewDto.setGroupId(group.getId() != null ? group.getId().toString() : "");
                                    groupPreviewDto.setGroupName(group.getName());
                                    groupPreviewDto.setLastMessage(groupMessage.getMessage());

                                    if (group.getLastMessageTimestamp() != null) {
                                        groupPreviewDto.setLastMessageTimeStamp(
                                            TimeUtil.formatDateTime(group.getLastMessageTimestamp().toLocalDateTime())
                                        );
                                    }

                                    var fileUpload = fileUploadRepository.findByReference(group.getIconImageRef());
                                    fileUpload.ifPresent(upload -> groupPreviewDto.setGroupImageUrl(upload.getPublicUrl()));

                                    return groupPreviewDto;
                                }
                            ).toList();
                    }
                    catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                }
            )
            .collectList().toFuture().get()
            .stream()
            .flatMap(List::stream)
            .toList();
    }


}


