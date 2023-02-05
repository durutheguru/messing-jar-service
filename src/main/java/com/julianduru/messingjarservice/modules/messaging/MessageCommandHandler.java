package com.julianduru.messingjarservice.modules.messaging;

import com.julianduru.util.api.OperationStatus;
import reactor.core.publisher.Mono;

/**
 * created by julian on 21/01/2023
 */
public interface MessageCommandHandler {


    Mono<OperationStatus<String>> handle(MessageCommand command) throws Exception;


    boolean supports(MessageCommand command);


}

