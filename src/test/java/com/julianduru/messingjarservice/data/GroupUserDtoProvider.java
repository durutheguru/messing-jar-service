package com.julianduru.messingjarservice.data;

import com.julianduru.messingjarservice.dto.GroupUserDto;
import com.julianduru.util.test.DataProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Component;

import java.util.concurrent.CompletableFuture;

/**
 * created by julian on 10/02/2023
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class GroupUserDtoProvider implements DataProvider<GroupUserDto> {


    private final UserDataProvider userDataProvider;


    private final GroupDataProvider groupDataProvider;



    @Override
    public GroupUserDto provide() {
        try {
            var userMono = userDataProvider.save();
            var groupMono = groupDataProvider.save();

            var user = userMono.toFuture().get();
            var group = groupMono.toFuture().get();

            if (user == null || group == null) {
                throw new IllegalStateException("Cannot Continue. User or Group is null");
            }

            var groupUserDto = new GroupUserDto();

            groupUserDto.setUserId(user.getId().toString());
            groupUserDto.setGroupId(group.getId().toString());

            return groupUserDto;
        }
        catch (Throwable t) {
            log.error(t.getMessage(), t);
            return null;
        }
    }


}
