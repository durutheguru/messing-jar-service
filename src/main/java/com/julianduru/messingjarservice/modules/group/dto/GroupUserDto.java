package com.julianduru.messingjarservice.modules.group.dto;

import com.julianduru.messingjarservice.dto.BaseDto;
import com.julianduru.messingjarservice.entities.GroupUser;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * created by julian on 10/02/2023
 */
@Data
public class GroupUserDto extends BaseDto {


    @NotNull(message = "Group ID is required")
    private String groupId;


    @NotNull(message = "User ID is required")
    private String userId;


    public static GroupUserDto fromEntity(GroupUser groupUser) {
        var dto = new GroupUserDto();

        dto.setId(groupUser.getIdString());
        dto.setUserId(groupUser.getUserId().toString());
        dto.setGroupId(groupUser.getGroupId().toString());

        return dto;
    }


}


