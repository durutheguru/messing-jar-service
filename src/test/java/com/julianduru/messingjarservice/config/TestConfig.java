package com.julianduru.messingjarservice.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.web.reactive.server.WebTestClientBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import reactor.core.publisher.Mono;

/**
 * created by julian on 31/08/2022
 */
@Slf4j
@TestConfiguration
public class TestConfig {


    private ExchangeFilterFunction logRequest() {
        return ExchangeFilterFunction.ofRequestProcessor(
            clientRequest -> {
                var requestMono = Mono.just(clientRequest);
                log.info("Request: {} {}", clientRequest.method(), clientRequest.url());
                clientRequest.headers()
                    .forEach((name, values) -> values.forEach(value -> log.info("{}={}", name, value)));

                return requestMono;
            }
        );


//        return (clientRequest, next) -> {
//            log.info("Request: {} {}", clientRequest.method(), clientRequest.url());
//            clientRequest.headers()
//                .forEach((name, values) -> values.forEach(value -> log.info("{}={}", name, value)));
//            return next.exchange(clientRequest);
//        };
    }


    private ExchangeFilterFunction logResponse() {
        return ExchangeFilterFunction.ofResponseProcessor(clientResponse -> {
            var response = Mono.just(clientResponse);
            response
                .flatMap(res -> res.bodyToMono(String.class))
                .doOnNext(str -> log.info("Response: {}", str))
                .subscribe();

            return response;
        });
    }

//
//    private Request enhance(Request request) {
//        StringBuilder group = new StringBuilder();
//        request.onRequestBegin(theRequest -> {
//            // append request url and method to group
//        });
//        request.onRequestHeaders(theRequest -> {
//            for (HttpField header : theRequest.getHeaders()) {
//                // append request headers to group
//            }
//        });
//        request.onRequestContent((theRequest, content) -> {
//            // append content to group
//        });
//        request.onRequestSuccess(theRequest -> {
//            log.debug(group.toString());
//            group.delete(0, group.length());
//        });
//        group.append("\n");
//        request.onResponseBegin(theResponse -> {
//            // append response status to group
//        });
//        request.onResponseHeaders(theResponse -> {
//            for (HttpField header : theResponse.getHeaders()) {
//                // append response headers to group
//            }
//        });
//        request.onResponseContent((theResponse, content) -> {
//            // append content to group
//        });
//        request.onResponseSuccess(theResponse -> {
//            log.debug(group.toString());
//        });
//        return request;
//    }

//    @Bean
//    public WebTestClientBuilderCustomizer clientBuilderCustomizer() {
//        return builder -> builder
//            .filter(logRequest())
//            .filter(logResponse());
//    }


}
