package com.julianduru.messingjarservice.data;

import com.julianduru.messingjarservice.modules.user.dto.UserDataDto;
import com.julianduru.util.test.DataProvider;
import org.springframework.stereotype.Component;

/**
 * created by julian on 01/11/2022
 */
@Component
public class UserDataUpdateProvider implements DataProvider<UserDataDto> {


    @Override
    public UserDataDto provide() {
        var update = new UserDataDto();

        update.setEmail(faker.internet().emailAddress());
        update.setFirstName(faker.name().firstName());
        update.setLastName(faker.name().lastName());
        update.setEnableEmails(true);
        update.setProfilePhotoRef(faker.code().ean13());

        return update;
    }


}
