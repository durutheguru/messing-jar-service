package com.julianduru.messingjarservice.data.dto;

import com.julianduru.messingjarservice.modules.user.dto.UserDto;
import com.julianduru.util.test.DataProvider;
import org.springframework.stereotype.Component;

/**
 * created by julian on 28/08/2022
 */
@Component
public class UserDtoProvider implements DataProvider<UserDto> {


    @Override
    public UserDto provide() {
        var dto = new UserDto();
        dto.setUsername(faker.code().isbn10(false));
        return dto;
    }


}

