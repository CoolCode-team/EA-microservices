package com.ea.accountservice.dto;

import jakarta.validation.constraints.NotBlank;

public record CreateSchoolUnitDto(
        @NotBlank String name,
        @NotBlank String type,
        String state,
        String city,
        String address,
        String contactNumber
) {}
