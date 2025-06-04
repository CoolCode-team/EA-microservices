package com.example.reserva.domain.application.dto;

import java.time.OffsetDateTime;

import com.example.reserva.domain.enterprise.entity.ReservationStatus;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReservationResponseDto {

    private String reservationId;

    private OffsetDateTime startDateTime;

    private OffsetDateTime endDateTime;

    private ReservationStatus status;

    // Dados do usuário (resumidos)
    private String userId;
    private String userName;

    // Dados do espaço acadêmico (resumidos)
    private String academicSpaceId;
    private String academicSpaceName;
}
