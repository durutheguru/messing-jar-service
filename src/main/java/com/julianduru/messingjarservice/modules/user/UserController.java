package com.julianduru.messingjarservice.modules.user;

import com.julianduru.messingjarservice.dto.UserDto;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import javax.validation.Valid;

/**
 * created by julian on 27/08/2022
 */
@RestController
@RequiredArgsConstructor
@RequestMapping(UserController.PATH)
public class UserController {


    public static final String PATH = "/user";


    private final UserService userService;


    @PostMapping
    public Mono<UserDto> saveUser(@Valid @RequestBody UserDto userDto) {
        return userService.saveUser(userDto).map(UserDto::fromEntity);
    }


}
