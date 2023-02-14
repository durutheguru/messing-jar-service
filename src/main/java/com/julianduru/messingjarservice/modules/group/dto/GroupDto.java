package com.julianduru.messingjarservice.modules.group.dto;

import com.julianduru.messingjarservice.dto.BaseDto;
import com.julianduru.messingjarservice.entities.Group;
import lombok.Data;
import org.bson.types.ObjectId;

import javax.validation.constraints.NotEmpty;

/**
 * created by julian on 10/02/2023
 */
@Data
public class GroupDto extends BaseDto {


    @NotEmpty(message = "Group name is required")
    private String name;


    private String iconImageRef;


    public Group toEntity() {
        var group = new Group();

        if (getId() != null) {
            group.setId(new ObjectId(getId()));
        }

        group.setName(getName());
        group.setIconImageRef(getIconImageRef());

        return group;
    }


    public static GroupDto fromEntity(Group group) {
        var dto = new GroupDto();

        dto.setName(group.getName());
        dto.setIconImageRef(group.getIconImageRef());
        dto.setId(group.getIdString());

        return dto;
    }


}


