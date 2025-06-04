package com.ea.backend.domain.reservation.application.repository;

import com.ea.backend.domain.reservation.enterprise.entity.Reservation;
import com.ea.backend.domain.reservation.enterprise.entity.ReservationStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

public interface ReservationRepository extends JpaRepository<Reservation, UUID> {

    @Query(
            "SELECT r FROM reservations r WHERE r.academicSpace.id = :academicSpaceId AND " +
                    "(r.startDateTime < :endDateTime AND r.endDateTime > :startDateTime)" + " AND r.status = 'SCHEDULED' "
    )
    List<Reservation> findOverlappingReservations(
            @Param("academicSpaceId") UUID academicSpaceId,
            @Param("startDateTime") OffsetDateTime startDateTime,
            @Param("endDateTime") OffsetDateTime endDateTime
    );

    Optional<Reservation> findReservationById(String id);

    @Query("SELECT r FROM reservations r WHERE r.endDateTime < :now AND r.status = 'SCHEDULED'")
    List<Reservation> findExpiredReservations(@Param("now") LocalDateTime now);

    Optional<Reservation> findReservationByIdAndUser_Id(String id, UUID userId);


    List<Reservation> findReservationsByUserIdAndStatus(UUID userId, ReservationStatus status);

  @Query(
      value =
          "SELECT EXTRACT(DOW FROM r.start_date_time) AS dayOfWeek, COUNT(r) AS count "
              + "FROM reservations r "
              + "WHERE r.start_date_time >= NOW() - INTERVAL '7 days' "
              + "GROUP BY dayOfWeek "
              + "ORDER BY dayOfWeek",
      nativeQuery = true)


  List<Map<String, Object>> countReservationsByDayOfWeek();


    Page<Reservation> findAllBy(PageRequest pageRequest);

  Page<Reservation> findAllByUserIdAndStatus(
      UUID userId, ReservationStatus status, PageRequest pageRequest);

  Page<Reservation> findAllByUserId(UUID userId, PageRequest pageRequest);

    Page<Reservation> findAllByStatus(ReservationStatus status, Pageable pageable);


    @Query(
            value = "SELECT r FROM reservations r " +
                    "JOIN r.user u " +
                    "WHERE u.name LIKE CONCAT('%', :name, '%')"
    )
    Page<Reservation> findAllByUserName(@Param("name") String name, Pageable pageable);


    @Query(
            value = "SELECT r FROM reservations r " +
                    "INNER JOIN r.academicSpace ac " +
                    "WHERE ac.roomName LIKE CONCAT('%', :name, '%')"
    )
    Page<Reservation> findAllByAcademicSpaceRoomName(@Param("name") String name, Pageable pageable);


}
