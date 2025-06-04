package com.ea.backend.infra.http;

import com.ea.backend.domain.space.application.service.AcademicSpaceService;
import com.ea.backend.infra.http.model.ListResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/spaces")
@PreAuthorize("hasAnyRole('ROLE_TEACHER', 'ROLE_ADMIN')")
@Tag(name = "AcademicSpaces", description = "Controller for Academic spaces")
public class AcademicSpaceController {

  private final AcademicSpaceService academicSpaceService;

  public AcademicSpaceController(AcademicSpaceService academicSpaceService) {
    this.academicSpaceService = academicSpaceService;
  }

  @GetMapping("/available")
  public ResponseEntity<?> getAvailableSpaces() {
    var results = this.academicSpaceService.fetchAllAvailableAcademicSpaces();

    return ListResponse.build(results);
  }
}
