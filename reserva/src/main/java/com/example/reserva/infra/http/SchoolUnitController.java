package com.ea.backend.infra.http;

import com.ea.backend.domain.school.application.SchoolUnitService;
import com.ea.backend.domain.school.application.dto.CreateSchoolUnitDto;
import com.ea.backend.domain.school.enterprise.entity.SchoolUnit;
import com.ea.backend.domain.user.application.repository.UserProjection;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;


@RestController
@RequestMapping("/school-units")
@PreAuthorize("hasAnyRole('ROLE_ADMIN')")
@Tag(name = "SchoolUnits")
public class SchoolUnitController {

    private final SchoolUnitService service;

    public SchoolUnitController(SchoolUnitService service) {
        this.service = service;
    }


    @PostMapping
    public SchoolUnit create(@RequestBody @Valid CreateSchoolUnitDto dto) {
        return service.create(dto);
    }

    @GetMapping
    public List<SchoolUnit> listAll() {
        return service.findAll();
    }


    @GetMapping("/{schoolUnitId}/teachers")
    @ApiResponse(responseCode = "200")
    public ResponseEntity<List<UserProjection>> getTeachersBySchoolUnit(
            @Valid
            @PathVariable String schoolUnitId) {

        return ResponseEntity.ok(this.service.getTeachersBySchoolUnit(UUID.fromString(schoolUnitId)));
    }

    @DeleteMapping("/{schoolUnitId}")
    public ResponseEntity<String> delete(@PathVariable String schoolUnitId) {
        service.delete(UUID.fromString(schoolUnitId));
        return ResponseEntity.ok("School unit deleted successfully");
    }


}
