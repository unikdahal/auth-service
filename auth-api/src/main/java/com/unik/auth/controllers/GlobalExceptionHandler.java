package com.unik.auth.controllers;

import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.http.HttpStatus;
import com.unik.auth.domain.exceptions.AuthenticationException;
import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public Map<String, Object> handleValidationException(MethodArgumentNotValidException ex) {
        Map<String, Object> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error ->
            errors.put(error.getField(), error.getDefaultMessage())
        );
        Map<String, Object> response = new HashMap<>();
        response.put("status", 400);
        response.put("error", "Validation failed");
        response.put("details", errors);
        return response;
    }

    @ExceptionHandler(AuthenticationException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ResponseBody
    public Map<String, Object> handleAuthException(AuthenticationException ex) {
        Map<String, Object> response = new HashMap<>();
        response.put("status", 401);
        response.put("error", "Authentication error");
        response.put("message", ex.getMessage());
        return response;
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ResponseBody
    public Map<String, Object> handleGenericException(Exception ex) {
        Map<String, Object> response = new HashMap<>();
        response.put("status", 500);
        response.put("error", "Internal server error");
        response.put("message", ex.getMessage());
        return response;
    }
}

