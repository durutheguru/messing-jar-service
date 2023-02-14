package com.julianduru.messingjarservice.util;

import com.julianduru.messingjarservice.entities.User;
import com.julianduru.messingjarservice.modules.user.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import reactor.core.publisher.Mono;

/**
 * created by julian on 30/10/2022
 */
public class AuthUtil {


    public static Mono<Authentication> authR() {
        return ReactiveSecurityContextHolder.getContext()
            .map(SecurityContext::getAuthentication);
    }


    public static Authentication auth() {
        return SecurityContextHolder.getContext().getAuthentication();
    }


    public static Mono<User> authUser(UserRepository userRepository) {
        return authR()
            .flatMap(
                authentication -> {
                    var username = ((User)authentication.getPrincipal()).getUsername();
                    return userRepository.findByUsername(username);
                }
            );
    }


}
