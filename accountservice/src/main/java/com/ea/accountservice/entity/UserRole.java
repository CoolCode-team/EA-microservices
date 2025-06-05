package com.ea.accountservice.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@Getter
@Schema(description = "Role of user")
public enum UserRole {

    @Schema(description = "Grant all access")
    ADMIN("admin"),

    @Schema(description = "Teacher access")
    TEACHER("teacher");

    private final String role;

    UserRole(String role) {
        this.role = role;
    }

}
