package com.ea.accountservice.http;

import com.ea.accountservice.dto.CreateUserDto;
import com.ea.accountservice.dto.UpdateUserDto;
import com.ea.accountservice.http.model.PaginatedResponseBuilder;
import com.ea.accountservice.repository.UserProjection;
import com.ea.accountservice.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.apache.coyote.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin")
@Tag(name = "admin", description = "Admin routes")
public class AdminController {

    @Autowired
    private  UserService userService;

    @PostMapping("/users")
    @Operation
    public ResponseEntity<?> createUser(@RequestBody @Valid CreateUserDto dto) throws BadRequestException {


        System.out.println(dto.getSchoolUnitId());


        switch (dto.toDomainRole()) {
            case ADMIN -> this.userService.createAdminUser(dto);
            case TEACHER -> this.userService.createTeacher(dto);
            default -> {
                throw new BadRequestException("Invalid user role");
            }
        }

        return ResponseEntity.ok().body("User created successfully");

    }


    @GetMapping("/users")
    @Operation
    public ResponseEntity<PaginatedResponseBuilder<UserProjection>> fetchTeachersPaginated(
            @Valid @RequestParam("page") int page, @RequestParam("pageSize") int pageSize) {

        return ResponseEntity.ok(
                new PaginatedResponseBuilder<>(this.userService.fetchUsersPaginated(page - 1, pageSize))
        );
    }


    @PutMapping("/users/{userId}")
    @Operation
    public ResponseEntity<?> updateUser(
            @Valid @RequestBody UpdateUserDto body, @PathVariable String userId) {

        this.userService.updateUser(userId, body);

        return ResponseEntity.ok().body("User updated successfully");
    }


    @DeleteMapping("/users/{userId}")
    @Operation
    public ResponseEntity<?> deleteUser(@PathVariable String userId) {
        this.userService.deleteUser(userId);
        return ResponseEntity.ok().body("User deleted successfully");
    }


}
