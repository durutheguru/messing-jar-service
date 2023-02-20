package com.julianduru.messingjarservice.modules.group;

import com.julianduru.fileuploader.repositories.FileUploadRepository;
import com.julianduru.messingjarservice.entities.GroupMessage;
import com.julianduru.messingjarservice.modules.group.dto.*;
import com.julianduru.messingjarservice.entities.Group;
import com.julianduru.messingjarservice.entities.GroupUser;
import com.julianduru.messingjarservice.entities.User;
import com.julianduru.messingjarservice.modules.user.UserRepository;
import com.julianduru.messingjarservice.modules.user.UserService;
import com.julianduru.messingjarservice.util.AuthUtil;
import com.julianduru.util.TimeUtil;
import com.julianduru.util.exception.RuntimeServiceException;
import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.security.Principal;
import java.util.List;
import java.util.Map;
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


    private final UserService userService;



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
    public List<GroupPreviewDto> fetchGroupPreviews(String username, int page, int size) throws ExecutionException, InterruptedException {
        return userRepository.findByUsername(username)
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
                            .map(id -> {
                                var groupMessage = groupMessageRepository
                                    .findFirstByGroupIdOrderByCreatedDateDesc(id)
                                    .toFuture().join();
                                return new Object[]{id, groupMessage};
                            })
                            .map(
                                objects -> {
                                    var groupId = (ObjectId) objects[0];
                                    var groupMessage = (GroupMessage) objects[1];
                                    var group = groups.stream().filter(g -> g.getId().equals(groupId)).findFirst().get();
                                    var groupPreviewDto = new GroupPreviewDto();

                                    groupPreviewDto.setGroupId(group.getId() != null ? group.getId().toString() : "");
                                    groupPreviewDto.setGroupName(group.getName());

                                    if (groupMessage != null) {
                                        groupPreviewDto.setLastMessage(groupMessage.getMessage());
                                    }

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


    @Override
    public GroupDetailsDto fetchGroupDetails(String groupId) throws ExecutionException, InterruptedException {
        var group = groupRepository.findById(new ObjectId(groupId)).toFuture().join();
        if (group == null) {
            throw new RuntimeServiceException(
                String.format("Group with id %s not found", groupId)
            );
        }

        var memberCount = groupUserRepository.countByGroupId(new ObjectId(groupId)).toFuture().join();
        return GroupDetailsDto.builder()
            .id(group.getId() != null ? group.getId().toString() : "")
            .name(group.getName())
            .memberCount(memberCount.intValue())
            .build();
    }


    @Override
    public Flux<GroupUserPreviewDto> fetchGroupUsers(String groupId) {
        return groupUserRepository.findGroupUsersByGroupId(
                new ObjectId(groupId)
            )
            .flatMap(
                groupUser -> userRepository.findById(groupUser.getUserId())
                    .map(
                        user -> {
                            var details = userService.fetchUserDetails(user.getUsername())
                                .toFuture().join();
                            return new GroupUserPreviewDto(
                                groupUser.getGroupId().toString(),
                                user.getUsername(),
                                details != null ? details.getProfilePhotoPublicUrl() : null
                            );
                        }
                    )
            );
    }


}


