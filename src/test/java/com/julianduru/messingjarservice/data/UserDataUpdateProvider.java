package com.julianduru.messingjarservice.data;

import com.julianduru.data.messaging.dto.UserDataUpdate;
import com.julianduru.messingjarservice.modules.user.dto.UserUpdateDto;
import com.julianduru.util.test.DataProvider;
import org.springframework.stereotype.Component;

/**
 * created by julian on 01/11/2022
 */
@Component
public class UserDataUpdateProvider implements DataProvider<UserUpdateDto> {


    @Override
    public UserUpdateDto provide() {
        var update = new UserUpdateDto();

        update.setEmail(faker.internet().emailAddress());
        update.setFirstName(faker.name().firstName());
        update.setLastName(faker.name().lastName());
        update.setEnableEmails(true);
        update.setProfilePhotoRef(faker.code().ean13());

        return update;
    }


}
