package com.ea.reservations.http;

import com.ea.reservations.dto.CreateReservationDto;
import com.ea.reservations.entity.Reservation;
import com.ea.reservations.http.model.PaginatedResponseBuilder;
import com.ea.reservations.http.model.UserAuthenticated;
import com.ea.reservations.service.ReservationService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/reservations")
public class ReservationController {
    @Autowired
    private ReservationService reservationService;

    @PatchMapping("/{reservationId}/checkout")
    public ResponseEntity<?> updateReservationStatus(@PathVariable String reservationId, HttpServletRequest request) {
        try {
            var user = (UserAuthenticated) request.getAttribute("user");

            this.reservationService.markReservationAsCheckedOut(UUID.fromString(reservationId), UUID.fromString(user.getUser().getId()));

            return ResponseEntity.ok().body("Reservation updated successfully");

        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping
    public ResponseEntity<?> createReservation(@RequestBody @Valid CreateReservationDto dto, HttpServletRequest request) {
        var authenticatedUser = (UserAuthenticated) request.getAttribute("user");

        dto.setUserId(authenticatedUser.getUser().getId().toString());

        this.reservationService.createReservation(dto);

        return ResponseEntity.ok().body("Reservation created successfully");
    }

    @GetMapping
    public ResponseEntity<PaginatedResponseBuilder<Reservation>> fetchUserReservationsPaginated(
            HttpServletRequest req,
            @Valid @RequestParam("page") int page,
            @RequestParam("pageSize") int pageSize,
            @RequestParam(value = "status", required = false) Optional<String> status) {

        var authenticatedUser = (UserAuthenticated) req.getAttribute("user");

        return ResponseEntity.ok(new PaginatedResponseBuilder<>(
                this.reservationService.fetchReservationByUserIdAndStatusPaged(
                        UUID.fromString(authenticatedUser.getUser().getId()), status, page - 1, pageSize)));
    }
}
