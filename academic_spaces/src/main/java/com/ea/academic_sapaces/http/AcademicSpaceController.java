package com.ea.academic_sapaces.http;

import com.ea.academic_sapaces.dto.ChangeSpaceStatusDto;
import com.ea.academic_sapaces.dto.CreateAcademicSpaceDto;
import com.ea.academic_sapaces.entity.AcademicSpace;
import com.ea.academic_sapaces.http.model.ListResponse;
import com.ea.academic_sapaces.http.model.PaginatedResponseBuilder;
import com.ea.academic_sapaces.service.AcademicSpaceService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/spaces")
@Tag(name = "AcademicSpaces", description = "Controller for Academic spaces")
public class AcademicSpaceController {

    private final AcademicSpaceService academicSpaceService;

    public AcademicSpaceController(AcademicSpaceService academicSpaceService) {
        this.academicSpaceService = academicSpaceService;
    }

    @GetMapping
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

    @GetMapping("/available")
    public ResponseEntity<?> getAvailableSpaces() {
        var results = this.academicSpaceService.fetchAllAvailableAcademicSpaces();

        return ListResponse.build(results);
    }



    @GetMapping("/all")
    @Operation
    @ApiResponse(responseCode = "200")
    public ResponseEntity<?> listAllSpaces() {
        return ResponseEntity.ok(this.academicSpaceService.fetchAllSpaces());
    }



    @PostMapping
    @Operation
    @ApiResponse(responseCode = "201")
    public ResponseEntity<?> createSpace(@RequestBody @Valid CreateAcademicSpaceDto dto) {
        this.academicSpaceService.createSpace(dto);

        return ResponseEntity.ok().body("Space created successfully");

    }

    @Operation
    @ApiResponse(responseCode = "201")
    @PatchMapping("/{spaceId}/status")
    public ResponseEntity<?> changeSpaceStatus(@PathVariable String spaceId, @RequestBody @Valid ChangeSpaceStatusDto requestBody) {

        try {
            requestBody.setSpaceId(spaceId);

            this.academicSpaceService.changeSpaceStatus(requestBody.getSpaceId(), requestBody.getStatus());

            return ResponseEntity.ok().body("Space status changed successfully");
        }catch (RuntimeException e) {
            throw new RuntimeException(e);
        }
    }



}
