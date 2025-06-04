package com.example.reserva.domain.application.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.UUID;

import java.time.OffsetDateTime;

@Setter
@Getter
public class CreateReservationDto {
    @UUID
    @NotBlank
    @NotNull
    private String academicSpaceId;


    @NotNull
    private OffsetDateTime startDateTime;

    @NotNull
    private OffsetDateTime endDateTime;
    
    private String userId;

}
