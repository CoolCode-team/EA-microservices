package user.user.domain.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record UpdateUserDto(
    @NotNull @Email String email,
    @NotNull String name,
    @NotNull String role,
    String contactNumber,
    String course,
    String schoolUnitId // Pode ser String se você quiser tratar como texto antes de converter para UUID
) {}

