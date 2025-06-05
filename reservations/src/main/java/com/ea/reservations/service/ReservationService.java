package com.ea.reservations.service;


import com.ea.reservations.dto.CreateReservationDto;
import com.ea.reservations.dto.UpdateReservationDto;
import com.ea.reservations.entity.Reservation;
import com.ea.reservations.entity.ReservationStatus;
import com.ea.reservations.exception.DomainException;
import com.ea.reservations.exception.DomainExceptionCode;
import com.ea.reservations.repository.ReservationRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class ReservationService {
    @Autowired
    private ReservationRepository reservationRepository;



    @Autowired
    private ApplicationEventPublisher applicationEventPublisher;


    private static boolean isIsValidReservation(Reservation reservation) {
        return reservation.getEndDateTime().isAfter(reservation.getStartDateTime());
    }


    @Transactional
    public void createReservation(CreateReservationDto dto) {

        var reservation = new Reservation();

      /*
      *   if(!academicSpace.isAvailable()) {
            throw new DomainException("Space is not available");
        }
      * */

        var userHasPendingReservation = this.reservationRepository.findReservationsByUserIdAndStatus(UUID.fromString(dto.getUserId()), ReservationStatus.SCHEDULED);

        if (!userHasPendingReservation.isEmpty()) {
            throw new DomainException("User already has a pending reservation", DomainExceptionCode.USER_HAS_PENDING_RESERVATIONS);
        }


        reservation.setUserId(UUID.fromString(dto.getUserId()));
        reservation.setAcademicSpaceId(UUID.fromString(dto.getAcademicSpaceId()));
        reservation.setStartDateTime(dto.getStartDateTime().withOffsetSameInstant(ZoneOffset.UTC));
        reservation.setEndDateTime(dto.getEndDateTime().withOffsetSameInstant(ZoneOffset.UTC));
        reservation.setStatus(ReservationStatus.SCHEDULED);

        var isValidReservation = isIsValidReservation(reservation);

        if (!isValidReservation) {
            throw new DomainException("Invalid reservation interval", DomainExceptionCode.DOMAIN_ERROR);
        }


        var overlappingReservations = this.reservationRepository.findOverlappingReservations(
                UUID.fromString(dto.getAcademicSpaceId()),
                reservation.getStartDateTime(),
                reservation.getEndDateTime()
        );


        if(!overlappingReservations.isEmpty()) {
            throw new DomainException("There is already a reservation for this space in this period", DomainExceptionCode.RESERVATION_INTERVAL_OVERLAP);
        }

        this.reservationRepository.save(reservation);

    }


    @Transactional
    public void cancelReservation(UUID reservationId) {
        var reservation = this.reservationRepository.findReservationById(reservationId.toString())
                .orElseThrow(() -> new DomainException("Reservation not found"));

        if (List.of(ReservationStatus.CONFIRMED_BY_THE_ENTERPRISE, ReservationStatus.CONFIRMED_BY_THE_ENTERPRISE).contains(reservation.getStatus())) {
            throw new DomainException("Not possible to cancel a finished reservation");
        }

        reservation.setStatus(ReservationStatus.CANCELED);

        this.reservationRepository.save(reservation);

    }

    public void markReservationAsCheckedOut(UUID reservationId, UUID userId) {
        var reservation = this.reservationRepository.findReservationByIdAndUserId(reservationId.toString(), userId)
                .orElseThrow(() -> new DomainException("Reservation not found"));


        if (reservation.isConfirmed()) {
            throw new DomainException("Not possible to checkout a finished reservation");
        }

        reservation.setStatus(ReservationStatus.CONFIRMED_BY_THE_USER);

        this.reservationRepository.save(reservation);
    }

    @Transactional
    public void updateExpiredReservations() {
        List<Reservation> expiredReservations = reservationRepository.findExpiredReservations(LocalDateTime.now());

        if (!expiredReservations.isEmpty()) {
            expiredReservations.forEach(reservation -> reservation.setStatus(ReservationStatus.CONFIRMED_BY_THE_ENTERPRISE));
            reservationRepository.saveAll(expiredReservations);
        }
    }


    public Page<Reservation> fetchReservationByUserIdAndStatusPaged(
            UUID userId, Optional<String> status, int page, int pageSize) {


        var pageRequest = PageRequest.of(page, pageSize, Sort.by("createdAt").descending());


        if (status.isPresent()) {
            return this.reservationRepository.findAllByUserIdAndStatus(
                    userId, ReservationStatus.valueOf(status.get()), pageRequest);
        }

        return this.reservationRepository.findAllByUserId(userId, pageRequest);
    }


    public Page<Reservation> fetchReservationWithFilterPaginated(
            int page,
            int pageSize,
            Optional<String> filterColumn,
            Optional<String> filterValue
    ) {

        var pageRequest = PageRequest.of(page, pageSize, Sort.by("createdAt").descending());

        if (filterColumn.isEmpty() || filterValue.isEmpty()) {
            return this.reservationRepository.findAll(pageRequest);
        }

        var column = filterColumn.get();
        var value = filterValue.get();

        switch (column) {
            case "status" -> {
                return this.reservationRepository.findAllByStatus(ReservationStatus.valueOf(value), pageRequest);
            }
            case "teacher" -> {
                return this.reservationRepository.findAllByUserName(value, pageRequest);
            }
            case "space" -> {
                return this.reservationRepository.findAllByAcademicSpaceRoomName(value, pageRequest);
            }
            default -> throw new DomainException("Invalid filter column");
        }

    }

    public void updateReservation(UpdateReservationDto dto) {




        var currentReservation = this.reservationRepository.findReservationById(dto.getReservationId().toString())
                .filter(reservation -> reservation.getUserId().equals(dto.getUserId().toString())) // user is the owner
                .filter(Reservation::isScheduled) // reservation is scheduled
                .orElseThrow(() -> new DomainException("Invalid Reservation Operation"));



        currentReservation.setAcademicSpaceId(UUID.fromString(dto.getAcademicSpaceId()));
        currentReservation.setStartDateTime(dto.getStartDateTime());
        currentReservation.setEndDateTime(dto.getEndDateTime());
        currentReservation.setStatus(ReservationStatus.SCHEDULED);

        var isValidReservation = isIsValidReservation(currentReservation);

        if (!isValidReservation) {
            throw new DomainException("Invalid reservation interval");
        }


        var overlappingReservationsWithoutCurrentReservation = this.reservationRepository.findOverlappingReservations(
                        currentReservation.getAcademicSpaceId(),
                        currentReservation.getStartDateTime(),
                        currentReservation.getEndDateTime()
                )
                .stream()
                .filter(reservation -> !reservation.getId().equals(currentReservation.getId())) // Exclude currentReservation
                .toList();

        if (!overlappingReservationsWithoutCurrentReservation.isEmpty()) {
            throw new DomainException("There is already a reservation for this space in this period", DomainExceptionCode.RESERVATION_INTERVAL_OVERLAP);
        }


        this.reservationRepository.save(currentReservation);


    }

}
