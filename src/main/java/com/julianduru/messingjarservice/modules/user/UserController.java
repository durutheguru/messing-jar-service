package com.julianduru.messingjarservice.modules.user;

import com.julianduru.messingjarservice.ServiceConstants;
import com.julianduru.messingjarservice.modules.user.dto.UserDto;
import com.julianduru.messingjarservice.modules.user.dto.UserDataDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import javax.validation.Valid;
import java.security.Principal;

/**
 * created by julian on 27/08/2022
 */
@RestController
@RequiredArgsConstructor
@RequestMapping(UserController.PATH)
public class UserController {


    public static final String PATH = ServiceConstants.API_BASE + "/user";


    private final UserService userService;


    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<UserDto> saveUser(@Valid @RequestBody UserDto userDto) {
        return userService.saveUser(userDto).map(UserDto::fromEntity);
    }


    @PatchMapping
    public Mono<Void> updateUser(
        @Valid @RequestBody UserDataDto userDataDto, @AuthenticationPrincipal Principal principal
    ) {
        return userService.updateUser(principal.getName(), userDataDto);
    }


    @GetMapping
    public Mono<UserDataDto> fetchUserDetails(@AuthenticationPrincipal Principal principal) {
        return userService.fetchUserDetails(principal.getName());
    }


}
