package user.user.infra.http.controller;

import user.user.domain.dto.UserAuthenticated;
import user.user.domain.dto.UserResponse;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/users")
@PreAuthorize("hasAnyRole('ROLE_TEACHER', 'ROLE_ADMIN')")
@Tag(name = "Users", description = "Ge Current User auth")
public class UserController {

  @GetMapping("/me")
  public ResponseEntity<UserResponse> getAuthenticatedUser(HttpServletRequest request) {

    var authenticated = (UserAuthenticated) request.getAttribute("user");

    var user = authenticated.getUser();

    return ResponseEntity.ok()
            .body(new UserResponse(
                    user.getId().toString(),
                    user.getEmail(),
                    user.getName(),
                    user.getRole())
            );
  }
}