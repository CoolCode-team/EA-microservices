package user.user.domain.dto;

public record UserResponse(
    String id,
    String email,
    String name,
    String role
) {}

