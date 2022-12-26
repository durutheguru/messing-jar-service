package com.julianduru.messingjarservice.modules.user;

import com.julianduru.messingjarservice.dto.UserDto;
import com.julianduru.messingjarservice.entities.User;
import com.julianduru.messingjarservice.modules.user.dto.UserDataDto;
import reactor.core.publisher.Mono;

/**
 * created by julian on 27/08/2022
 */
public interface UserService {


    Mono<User> saveUser(UserDto userDto);


    Mono<Void> updateUser(String username, UserDataDto userDataDto);


    Mono<UserDataDto> fetchUserDetails(String username);


}


