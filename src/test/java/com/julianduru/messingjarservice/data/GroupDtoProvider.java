package com.julianduru.messingjarservice.data;

import com.julianduru.messingjarservice.modules.group.dto.GroupDto;
import com.julianduru.util.test.DataProvider;
import org.springframework.stereotype.Component;

/**
 * created by julian on 10/02/2023
 */
@Component
public class GroupDtoProvider implements DataProvider<GroupDto> {


    @Override
    public GroupDto provide() {
        var dto = new GroupDto();
        dto.setName(faker.name().fullName() + " Group");
        dto.setIconImageRef(faker.code().isbn10());
        return dto;
    }


}

