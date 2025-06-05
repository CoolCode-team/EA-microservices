package user.user.domain.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;

public record UpdateUserDto(
    @NotNull @Email String email, @NotNull String name, @NotNull String role) {}
