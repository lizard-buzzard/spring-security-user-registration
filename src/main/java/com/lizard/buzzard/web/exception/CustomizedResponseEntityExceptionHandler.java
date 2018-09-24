package com.lizard.buzzard.web.exception;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class CustomizedResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {

    @Autowired
    private MessageSource messageSource;

    @ExceptionHandler(value = {UserAlreadyExistException.class})
    public ResponseEntity<Object> userAlreadyExistsHandler(UserAlreadyExistException ex, WebRequest request) throws JsonProcessingException {
        Map<String, String> map = new HashMap<>();
        map.put("UserAlreadyExist", messageSource.getMessage("response.entity.exception.handler.user.already.exists", null, request.getLocale()));
        ResponseDetails responseDetailsBody = new ResponseDetails("error", map);
//        ObjectMapper mapper = new ObjectMapper();
//        String jsonInString = mapper.writeValueAsString(errorDetailsBody);

        return handleExceptionInternal(ex, responseDetailsBody, new HttpHeaders(), HttpStatus.CONFLICT, request);
    }
}
