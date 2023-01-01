package com.julianduru.messingjarservice.util;

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


}
