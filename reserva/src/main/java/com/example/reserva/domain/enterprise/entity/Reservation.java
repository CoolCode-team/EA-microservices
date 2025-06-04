package com.example.reserva.domain.enterprise.entity;

import java.io.Serializable;
import java.time.OffsetDateTime;

import com.example.reserva.shared.DomainEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@Table(name = "reservations")
@Entity(name = "reservations")
@AllArgsConstructor
@NoArgsConstructor
public class Reservation extends DomainEntity implements Serializable {

    @Id
    @GeneratedValue(strategy =  GenerationType.UUID)
    private String id;

    @Column(nullable = false, columnDefinition = "TIMESTAMP WITH TIME ZONE")
    private OffsetDateTime startDateTime;

    @Column(nullable = false, columnDefinition = "TIMESTAMP WITH TIME ZONE")
    private OffsetDateTime endDateTime;

    @Column(name = "academic_space_id", nullable = false)
    private String academicSpaceId;

    @Column(name = "user_id", nullable = false)
    private String userId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, columnDefinition = "VARCHAR(255) DEFAULT 'PENDING'")
    private ReservationStatus status;

    public boolean isScheduled() {
        return ReservationStatus.SCHEDULED.equals(this.status);
    }

    public boolean isCanceled() {
        return ReservationStatus.CANCELED.equals(this.status);
    }

    public boolean isConfirmedByTheUser() {
        return ReservationStatus.CONFIRMED_BY_THE_USER.equals(this.status);
    }

    public boolean isConfirmedByEnterprise() {
        return ReservationStatus.CONFIRMED_BY_THE_ENTERPRISE.equals(this.status);
    }

    public boolean isConfirmed() {
        return isConfirmedByTheUser() || isConfirmedByEnterprise();
    }

}
