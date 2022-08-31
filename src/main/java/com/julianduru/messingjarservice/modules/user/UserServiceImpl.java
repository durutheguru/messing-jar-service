package com.julianduru.messingjarservice.modules.user;

import com.julianduru.messingjarservice.dto.UserDto;
import com.julianduru.messingjarservice.entities.User;
import com.julianduru.messingjarservice.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

/**
 * created by julian on 27/08/2022
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {


    private final UserRepository userRepository;


    @Override
    public Mono<User> saveUser(UserDto userDto) {
        return userRepository.save(userDto.toEntity());
    }


}


