package user.user.infra.http.controller;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import user.user.domain.dto.MakeLoginDto;
import user.user.infra.security.TokenService;

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

    return ResponseEntity.ok(Map.of("accessToken", token));
}
}
