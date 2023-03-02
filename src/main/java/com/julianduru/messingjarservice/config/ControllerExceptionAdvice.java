package com.julianduru.messingjarservice.config;


import com.julianduru.util.api.ApiErrorResponse;
import com.julianduru.util.exception.EntityNotFoundException;
import com.julianduru.util.exception.InvalidClientRequestException;
import com.julianduru.util.exception.RuntimeServiceException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.support.WebExchangeBindException;

/**
 * created by julian
 */
@Slf4j
@ControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE)
public class ControllerExceptionAdvice  {


    @ExceptionHandler({Exception.class})
    public ResponseEntity<ApiErrorResponse> handleAllExceptions(Exception e) {
        log.error("Controller Exception: " + e.getMessage(), e);
        return new ResponseEntity<>(new ApiErrorResponse(e), HttpStatus.INTERNAL_SERVER_ERROR);
    }


    @ExceptionHandler({EntityNotFoundException.class})
    public ResponseEntity<ApiErrorResponse> handleEntityNotFoundException(EntityNotFoundException e) {
        log.error("Controller Exception: " + e.getMessage(), e);
        return new ResponseEntity<>(new ApiErrorResponse(e), HttpStatus.NOT_FOUND);
    }


    @ExceptionHandler({RuntimeServiceException.class})
    public ResponseEntity<ApiErrorResponse> handleRuntimeServiceExceptions(RuntimeServiceException e) {
        log.error("Controller Exception: " + e.getMessage(), e);
        return new ResponseEntity<>(new ApiErrorResponse(e), HttpStatus.INTERNAL_SERVER_ERROR);
    }


    @ExceptionHandler({
        IllegalArgumentException.class,
        WebExchangeBindException.class,
    })
    public ResponseEntity<ApiErrorResponse> handleInvalidInputUnprocessableException(Exception e) {
        log.error("Controller Exception: " + e.getMessage(), e);

        if (e instanceof WebExchangeBindException) {
            e = new RuntimeServiceException(
                ((WebExchangeBindException) e).getAllErrors().stream()
                    .map(DefaultMessageSourceResolvable::getDefaultMessage).toList()
                    .stream().findFirst().orElse("Invalid Input")
            );
        }

        return new ResponseEntity<>(new ApiErrorResponse(e), HttpStatus.UNPROCESSABLE_ENTITY);
    }


    @ExceptionHandler({
        InvalidClientRequestException.class,
    })
    public ResponseEntity<ApiErrorResponse> handleInvalidInputBadRequestException(Exception e) {
        log.error("Controller Exception: " + e.getMessage(), e);
        return new ResponseEntity<>(new ApiErrorResponse(e), HttpStatus.BAD_REQUEST);
    }


    @ExceptionHandler({AccessDeniedException.class})
    public ResponseEntity<ApiErrorResponse> handleAccessDeniedException(Exception e) {
        log.error("Controller Exception: " + e.getMessage(), e);
        return new ResponseEntity<>(new ApiErrorResponse(e), HttpStatus.FORBIDDEN);
    }


}



