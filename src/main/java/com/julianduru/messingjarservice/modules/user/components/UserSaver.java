package com.julianduru.messingjarservice.modules.user.components;

import com.julianduru.messingjarservice.dto.UserDto;
import com.julianduru.messingjarservice.entities.User;
import com.julianduru.messingjarservice.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

/**
 * created by julian on 26/12/2022
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class UserSaver {


    private final UserRepository userRepository;


    public void saveUser(UserDto userDto) {
        if (userRepository.existsByUsername(userDto.getUsername())) {
            return;
        }

        Mono.just(new User())
            .flatMap(u -> {
                u.setUsername(userDto.getUsername());
                return userRepository.save(u);
            })
            .subscribe();
    }


}


