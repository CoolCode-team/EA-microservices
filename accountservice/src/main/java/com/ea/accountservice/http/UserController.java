package com.ea.accountservice.http;


import com.ea.accountservice.http.model.UserAuthenticated;
import com.ea.accountservice.http.model.UserResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
@Tag(name ="users")
public class UserController {
    @GetMapping("/me")
    public ResponseEntity<UserResponse> getAuthenticatedUser(HttpServletRequest request) {

        var user = (UserAuthenticated) request.getAttribute("user");

        return ResponseEntity.ok()
                .body(new UserResponse(
                        user.getId().toString(),
                        user.getEmail(),
                        user.getName(),
                        user.getRole())
                );
    }


    @GetMapping("/health")
    public ResponseEntity<String> healthCheck() {
        return ResponseEntity.ok("User service is up and running");
    }

}
