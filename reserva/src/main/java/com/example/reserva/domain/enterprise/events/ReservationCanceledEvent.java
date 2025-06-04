package com.example.reserva.domain.enterprise.events;

import org.springframework.context.ApplicationEvent;

import com.example.reserva.domain.enterprise.entity.Reservation;

import lombok.Getter;

@Getter
public class ReservationCanceledEvent extends ApplicationEvent {
    private final Reservation reservation;

    public ReservationCanceledEvent(Reservation source) {
        super(source);
        this.reservation = source;
    }
}
