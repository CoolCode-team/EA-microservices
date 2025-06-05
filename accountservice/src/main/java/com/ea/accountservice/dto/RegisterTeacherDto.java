package com.ea.accountservice.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record RegisterTeacherDto(
        @NotBlank String name,
        @NotBlank String password,
        @NotBlank @Email String email

) {
}
