package com.ea.backend.infra.http;

import com.ea.backend.domain.user.application.dto.MakeLoginDto;
import com.ea.backend.infra.http.model.LoginResponseEntity;
import com.ea.backend.infra.security.TokenService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@PreAuthorize("permitAll()")
@Tag(name = "Auth")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final TokenService tokenService;

    public AuthController(AuthenticationManager authenticationManager, TokenService tokenService) {
        this.authenticationManager = authenticationManager;
        this.tokenService = tokenService;

    }

  @PostMapping("/sign-in")
  public ResponseEntity<?> login(@RequestBody @Valid MakeLoginDto dto) {
      var authentication = new UsernamePasswordAuthenticationToken(dto.getEmail(), dto.getPassword());
      var results = authenticationManager.authenticate(authentication);
      var token = tokenService.generateToken(results);

      return LoginResponseEntity.build(token);
    }
}