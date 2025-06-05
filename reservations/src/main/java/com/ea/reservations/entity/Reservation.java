package com.ea.reservations.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;
import java.util.UUID;


@Entity(name = "reservations")
@Data
@Table(name = "reservations")
@NoArgsConstructor
@AllArgsConstructor
public class Reservation {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(nullable = false, columnDefinition = "TIMESTAMP WITH TIME ZONE")
    private OffsetDateTime startDateTime;

    @Column(nullable = false, columnDefinition = "TIMESTAMP WITH TIME ZONE")
    private OffsetDateTime endDateTime;

    // Removido relacionamento com entidade externa
    @Column(name = "academic_space_id", nullable = false)
    private UUID academicSpaceId;

    @Column(name = "user_id", nullable = false)
    private UUID userId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, columnDefinition = "VARCHAR(255) DEFAULT 'PENDING'")
    private ReservationStatus status;

    // Métodos utilitários
    public boolean isScheduled() {
        return this.status.equals(ReservationStatus.SCHEDULED);
    }

    public boolean isCanceled() {
        return this.status.equals(ReservationStatus.CANCELED);
    }

    public boolean isConfirmedByTheUser() {
        return this.status.equals(ReservationStatus.CONFIRMED_BY_THE_USER);
    }

    public boolean isConfirmedByEnterprise() {
        return this.status.equals(ReservationStatus.CONFIRMED_BY_THE_ENTERPRISE);
    }

    public boolean isConfirmed() {
        return this.isConfirmedByTheUser() || this.isConfirmedByEnterprise();
    }
}
