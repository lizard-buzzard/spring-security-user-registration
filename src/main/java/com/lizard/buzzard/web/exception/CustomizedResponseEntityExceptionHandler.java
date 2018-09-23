package com.lizard.buzzard.web.exception;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class CustomizedResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {

    @Autowired
    private MessageSource messageSource;

    @ExceptionHandler(value = {UserAlreadyExistException.class})
    public ResponseEntity<Object> userAlreadyExistsHandler(UserAlreadyExistException ex, WebRequest request) {
        ErrorDetails errorDetailsBody = new ErrorDetails(
                messageSource.getMessage("response.entity.exception.handler.user.already.exists", null, request.getLocale()),
                "UserAlreadyExist");
        return handleExceptionInternal(ex, errorDetailsBody, new HttpHeaders(), HttpStatus.CONFLICT, request);
    }
}
