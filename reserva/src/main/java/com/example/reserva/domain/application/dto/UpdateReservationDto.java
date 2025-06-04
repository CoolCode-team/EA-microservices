package com.ea.backend.domain.reservation.application.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.UUID;

import java.time.OffsetDateTime;


@Setter
@Getter
public class UpdateReservationDto {
    @Setter
    @Getter

    @UUID
    @NotBlank
    @NotNull
    private String academicSpaceId;


    @NotNull
    private OffsetDateTime startDateTime;

    @NotNull
    private OffsetDateTime endDateTime;

    private java.util.UUID userId;

    private java.util.UUID reservationId;

}
