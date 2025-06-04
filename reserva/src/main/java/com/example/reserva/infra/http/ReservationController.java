package com.example.reserva.infra.http;


import com.example.reserva.domain.application.dto.CreateReservationDto;
import com.example.reserva.domain.application.dto.ReservationResponseDto;
import com.example.reserva.domain.application.services.ReservationService;
import com.example.reserva.infra.http.model.PaginatedResponseBuilder;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import java.util.UUID;

import com.example.reserva.domain.enterprise.entity.Reservation;

@RestController
@RequestMapping("/reservations")
@PreAuthorize("hasAnyRole('ROLE_TEACHER', 'ROLE_ADMIN')")
@Tag(name = "Reservations", description = "Controller for create and update reservations")
public class ReservationController {

    @Autowired
    private ReservationService reservationService;

    @PatchMapping("/{reservationId}/{userId}/checkout")
    public ResponseEntity<?> updateReservationStatus(@PathVariable UUID userId, String reservationId, HttpServletRequest request) {
        try {
            // var user = (UserAuthenticated) request.getAttribute("user");

            this.reservationService.markReservationAsCheckedOut(UUID.fromString(reservationId), userId);

            return ResponseEntity.ok().body("Reservation updated successfully");

        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping
    public ResponseEntity<?> createReservation(@RequestBody @Valid CreateReservationDto dto, HttpServletRequest request, @PathVariable String userId) {
        // var authenticatedUser = (UserAuthenticated) request.getAttribute("user");

        dto.setUserId(userId);

        this.reservationService.createReservation(dto);

        return ResponseEntity.ok().body("Reservation created successfully");
    }

  @GetMapping
  public ResponseEntity<PaginatedResponseBuilder<Reservation>> fetchUserReservationsPaginated(
      HttpServletRequest req,
      @Valid @RequestParam("page") int page,
      @RequestParam("pageSize") int pageSize,
      @RequestParam(value = "status", required = false) Optional<String> status,
      @PathVariable UUID userId) {

    // var authenticatedUser = (UserAuthenticated) req.getAttribute("user");

      return ResponseEntity.ok(new PaginatedResponseBuilder<>(
              this.reservationService.fetchReservationByUserIdAndStatusPaged(
                      userId, status, page - 1, pageSize)));
  }
}
