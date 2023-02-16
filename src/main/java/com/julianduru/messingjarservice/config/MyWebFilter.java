package com.julianduru.messingjarservice.config;


import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

import java.security.Principal;

//@Component
public class MyWebFilter {
    
//    @Override
//    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
////        Mono<Principal> principal = exchange.getPrincipal();
////        return principal.flatMap(p -> {
////            var authentication = new UsernamePasswordAuthenticationToken(p.getName(), "");
////            var securityContext = SecurityContextHolder.createEmptyContext();
////            securityContext.setAuthentication(authentication);
////            return chain.filter(exchange)
////                .contextWrite(
////                    ReactiveSecurityContextHolder.withSecurityContext(
////                        Mono.just(securityContext)
////                    )
////                );
////        }).switchIfEmpty(chain.filter(exchange));
//
//        var principal = exchange.getPrincipal();
//        return principal.flatMap(
//            p -> ReactiveSecurityContextHolder.getContext()
//                .map(securityContext -> {
//                    securityContext.setAuthentication(new UsernamePasswordAuthenticationToken(p.getName(), ""));
//                    return securityContext;
//                })
//            )
//            .switchIfEmpty(chain.filter(exchange));
//    }
}