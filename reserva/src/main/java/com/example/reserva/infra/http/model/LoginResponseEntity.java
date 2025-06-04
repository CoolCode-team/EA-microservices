package com.ea.backend.infra.http.model;

import org.springframework.http.ResponseEntity;

import java.util.Map;


public  class LoginResponseEntity  {

    public static ResponseEntity build(String token) {

        Map<String, String> response = Map.of("token", token );

        return ResponseEntity.ok(response);

    }
}
