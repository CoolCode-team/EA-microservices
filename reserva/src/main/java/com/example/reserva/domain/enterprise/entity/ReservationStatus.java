package com.ea.backend.domain.reservation.enterprise.entity;

public enum ReservationStatus {
    CONFIRMED_BY_THE_USER("CONFIRMED_BY_THE_USER"),
    CONFIRMED_BY_THE_ENTERPRISE("CONFIRMED_BY_THE_ENTERPRISE"),
    CANCELED("CANCELED"),
    SCHEDULED("SCHEDULED");

    private final String role;

    ReservationStatus(String status) {
        this.role = status;


    }

}
