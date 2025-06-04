package com.example.reserva.infra.http.exception;

import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.ea.backend.shared.DomainException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class HttpControllerExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<Map<String, String>> handleValidateArgumentException(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error ->
                errors.put(error.getField(), error.getDefaultMessage()));

        return ResponseEntity.badRequest().body(errors);
    }


    @ExceptionHandler(JWTVerificationException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ResponseEntity<Map<String, String>> handlerValidateSecurityException(JWTVerificationException ex) {

        Map<String, String> errors = new HashMap<>();

        errors.put("message", ex.getMessage());
        errors.put("code", String.valueOf(ex.hashCode()));

        return ResponseEntity.badRequest().body(errors);
    }


    @ExceptionHandler(DomainException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<Map<String, String>> handlerDomainException(DomainException ex) {

        Map<String, String> errors = new HashMap<>();

        errors.put("message", ex.getMessage());
        errors.put("code", String.valueOf(ex.getCode()));


        return ResponseEntity.badRequest().body(errors);
    }


    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<Map<String, String>> handlerMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException ex) {

        Map<String, String> errors = new HashMap<>();
        
        errors.put("message", ex.getMessage());
        errors.put("code", String.valueOf(ex.hashCode()));

        return ResponseEntity.badRequest().body(errors);

    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<Map<String, String>> handlerException(Exception ex) {

        Map<String, String> errors = new HashMap<>();

        errors.put("message", ex.getMessage());
        errors.put("code", String.valueOf(ex.hashCode()));

        return ResponseEntity.badRequest().body(errors);

    }


    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<Map<String, String>> handlerIllegalArguments(Exception ex) {

        Map<String, String> errors = new HashMap<>();

    errors.put("message", ex.getMessage());
    errors.put("path", ex.getCause().getMessage());
    errors.put("code", String.valueOf(ex.hashCode()));

    return ResponseEntity.badRequest().body(errors);
  }

  @ExceptionHandler(TokenExpiredException.class)
  @ResponseStatus(HttpStatus.UNAUTHORIZED)
  public ResponseEntity<Map<String, String>> handlerTokenExpiredException(
      TokenExpiredException ex) {

    Map<String, String> errors = new HashMap<>();

    errors.put("message", ex.getMessage());
    errors.put("code", String.valueOf(ex.hashCode()));

        return ResponseEntity.badRequest().body(errors);

    }


}
