package com.ea.backend.domain.reservation.enterprise.events;

import com.ea.backend.domain.reservation.enterprise.entity.Reservation;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class ReservationCanceledEvent extends ApplicationEvent {
    private final Reservation reservation;

    public ReservationCanceledEvent(Reservation source) {
        super(source);
        this.reservation = source;
    }
}
