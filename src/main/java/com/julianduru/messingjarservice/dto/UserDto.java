package com.julianduru.messingjarservice.dto;

import com.julianduru.messingjarservice.entities.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

/**
 * created by julian on 27/08/2022
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserDto extends BaseDto {


    @NotEmpty(message = "Username is required")
    @Size(max = 150, message = "Username should not exceed {max} characters")
    private String username;


    public static UserDto fromEntity(User user) {
        var dto = new UserDto();

        dto.setId(user.getIdString());
        dto.setCode(user.getCode());
        dto.setUsername(user.getUsername());

        return dto;
    }


    public User toEntity() {
        var user = new User();

        user.setCode(getCode());
        user.setUsername(getUsername());

        return user;
    }


}

