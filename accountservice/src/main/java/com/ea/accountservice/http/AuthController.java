package com.ea.accountservice.http;

import com.ea.accountservice.dto.MakeLoginDto;
import com.ea.accountservice.service.TokenService;
import com.ea.accountservice.service.UserService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/auth")
@Tag(name = "Auth")
public class AuthController {

    @Autowired
    private UserService userService;

    @Autowired
    private PasswordEncoder passwordEncoder;


    @Autowired
    private TokenService tokenService;

    @PostMapping("/sign-in")
    public ResponseEntity<?> login(@RequestBody @Valid MakeLoginDto dto) {

        var user = userService.findByEmail(dto.getEmail());

        if (user == null || !passwordEncoder.matches(dto.getPassword(), user.getPasswordHash())) {
            return ResponseEntity.status(401).body("Invalid email or password");
        }

        var jwtToken = tokenService.generateToken(user) ;

        return  ResponseEntity.status(200).body( Map.of("token", jwtToken));
    }

}
