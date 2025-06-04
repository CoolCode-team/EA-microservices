package com.ea.backend.infra.http;

import com.ea.backend.domain.reservation.application.dto.UpdateReservationDto;
import com.ea.backend.domain.reservation.application.services.ReservationService;
import com.ea.backend.domain.reservation.enterprise.entity.Reservation;
import com.ea.backend.domain.space.application.dto.ChangeSpaceStatusDto;
import com.ea.backend.domain.space.application.dto.CreateAcademicSpaceDto;
import com.ea.backend.domain.space.application.service.AcademicSpaceService;
import com.ea.backend.domain.space.enterprise.AcademicSpace;
import com.ea.backend.domain.user.application.MetricService;
import com.ea.backend.domain.user.application.UserService;
import com.ea.backend.domain.user.application.dto.CreateUserDto;
import com.ea.backend.domain.user.application.dto.UpdateUserDto;
import com.ea.backend.domain.user.application.repository.UserProjection;
import com.ea.backend.infra.http.model.PaginatedResponseBuilder;
import com.ea.backend.infra.security.UserAuthenticated;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.apache.coyote.BadRequestException;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import java.util.UUID;

@RestController
@PreAuthorize("hasRole('ROLE_ADMIN')")
@RequestMapping("/admin")
@Tag(name = "Admin", description = "Admin routes")
public class AdminController {

    private final UserService userService;
    private final AcademicSpaceService academicSpaceService;
    private  final ReservationService reservationService;
  private final MetricService metricService;

  public AdminController(
      UserService userService,
      AcademicSpaceService academicSpaceService,
      ReservationService reservationService,
      MetricService metricService) {
        this.userService = userService;
        this.academicSpaceService = academicSpaceService;
        this.reservationService = reservationService;
    this.metricService = metricService;
    }

  @PostMapping("/users")
  @Operation
  public ResponseEntity<?> createUser(@RequestBody @Valid CreateUserDto dto) throws BadRequestException {


      System.out.println(dto.getSchoolUnitId());


      switch (dto.toDomainRole()) {
          case ADMIN -> this.userService.createAdminUser(dto);
          case TEACHER -> this.userService.createTeacher(dto);
          default -> {
              throw new BadRequestException("Invalid user role");
          }
      }

      return ResponseEntity.ok().body("User created successfully");

  }

  @GetMapping("/users")
  @Operation
  public ResponseEntity<PaginatedResponseBuilder<UserProjection>> fetchTeachersPaginated(
      @Valid @RequestParam("page") int page, @RequestParam("pageSize") int pageSize) {

      return ResponseEntity.ok(
              new PaginatedResponseBuilder<>(this.userService.fetchUsersPaginated(page - 1, pageSize))
      );
  }

  @PutMapping("/users/{userId}")
  @Operation
  public ResponseEntity<?> updateUser(
      @Valid @RequestBody UpdateUserDto body, @PathVariable String userId) {

    this.userService.updateUser(userId, body);

    return ResponseEntity.ok().body("User updated successfully");
  }


    @DeleteMapping("/users/{userId}")
    @Operation
    public ResponseEntity<?> deleteUser(@PathVariable String userId) {
        this.userService.deleteUser(userId);
        return ResponseEntity.ok().body("User deleted successfully");
    }


  @GetMapping("/spaces")
  @Operation
  @ApiResponse(responseCode = "200")
  public ResponseEntity<PaginatedResponseBuilder<AcademicSpace>> getSpacesPaginated(
      @Valid @RequestParam("page") int page,
      @RequestParam("pageSize") int pageSize,
      @RequestParam(value = "nmFilterColumn", required = false) String filterField,
      @RequestParam(value = "nmFilterValue", required = false) String filterValue) {


      return ResponseEntity.ok(new PaginatedResponseBuilder<>(
              this.academicSpaceService.fetchSpacesPaginated(
                      page - 1, pageSize, filterField, filterValue))
      );

  }

    @GetMapping("/spaces/all")
    @Operation
    @ApiResponse(responseCode = "200")
    public ResponseEntity<?> listAllSpaces() {
        return ResponseEntity.ok(this.academicSpaceService.fetchAllSpaces());
    }


    @PostMapping("/spaces")
    @Operation
    @ApiResponse(responseCode = "201")
    public ResponseEntity<?> createSpace(@RequestBody @Valid CreateAcademicSpaceDto dto) {
            this.academicSpaceService.createSpace(dto);

            return ResponseEntity.ok().body("Space created successfully");

    }


    @Operation
    @ApiResponse(responseCode = "201")
    @PatchMapping("/spaces/{spaceId}/status")
    public ResponseEntity<?> changeSpaceStatus(@PathVariable String spaceId, @RequestBody @Valid ChangeSpaceStatusDto requestBody) {

       try {
           requestBody.setSpaceId(spaceId);

           this.academicSpaceService.changeSpaceStatus(requestBody.getSpaceId(), requestBody.getStatus());

           return ResponseEntity.ok().body("Space status changed successfully");
       }catch (RuntimeException e) {
                throw new RuntimeException(e);
            }
    }

  @GetMapping("/reservations")
  public ResponseEntity<PaginatedResponseBuilder<Reservation>> fetchReservationsPaged(
          @Valid
          @RequestParam("page") int page,
          @RequestParam("pageSize") int pageSize,
          @RequestParam(value = "nmFilterColumn", required = false) String filterColumn,
          @RequestParam(value = "nmFilterValue", required = false) String filterValue


  ) {
      return ResponseEntity.ok(new PaginatedResponseBuilder<>(
              this.reservationService.fetchReservationWithFilterPaginated(
                      page - 1, pageSize,
                      Optional.ofNullable(filterColumn),
                      Optional.ofNullable(filterValue)
              )));
  }


    @PatchMapping("/reservations/{reservationId}/cancel")
    public ResponseEntity<?> cancelReservation(@PathVariable @Valid String reservationId) {
        this.reservationService.cancelReservation(UUID.fromString(reservationId));
        return ResponseEntity.ok().body("Reservation cancelled successfully");

    }


    @PutMapping("/reservations/{reservationId}")
    public ResponseEntity<?> updateReservation(
            @PathVariable String reservationId,
            @RequestBody @Valid UpdateReservationDto reservation,
            HttpServletRequest request
    ) {
        var user = (UserAuthenticated) request.getAttribute("user");


        reservation.setUserId(user.getUser().getId());
        reservation.setReservationId(UUID.fromString(reservationId));

        this.reservationService.updateReservation(reservation);


        return ResponseEntity.ok().body("Reservation updated successfully");
    }


    @GetMapping("/metrics/count")
  public ResponseEntity<?> getMetrics() {
    return ResponseEntity.ok().body(this.metricService.getCounts());
  }

  @GetMapping("/metrics/reservations-by-day-of-week")
  public ResponseEntity<?> countReservationsByDayOfWeek() {
    return ResponseEntity.ok().body(this.metricService.countReservationsByDayOfWeek());
  }

  @GetMapping("/metrics/reservations-by-academic-space-last-7-days")
  public ResponseEntity<?> countReservationsByAcademicSpaceLast7Days() {
    return ResponseEntity.ok().body(this.metricService.countReservationsByAcademicSpaceLast7Days());
  }


  

}
