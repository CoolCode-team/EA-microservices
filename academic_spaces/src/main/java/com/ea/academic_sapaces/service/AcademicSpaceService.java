package com.ea.academic_sapaces.service;


import com.ea.academic_sapaces.dto.CreateAcademicSpaceDto;
import com.ea.academic_sapaces.entity.AcademicSpace;
import com.ea.academic_sapaces.entity.SpaceStatus;
import com.ea.academic_sapaces.exception.DomainException;
import com.ea.academic_sapaces.exception.DomainExceptionCode;
import com.ea.academic_sapaces.repository.AcademicSpaceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class AcademicSpaceService {
    @Autowired
    private AcademicSpaceRepository academicSpaceRepository;

    public void createSpace(CreateAcademicSpaceDto dto) {

        var space = new AcademicSpace();

        var existsSpaceWithName = this.academicSpaceRepository.findAcademicSpaceByAcronym(dto.getAcronym());

        existsSpaceWithName.ifPresent(
                (spaceFound) -> {
                    throw new DomainException(
                            "Space with this acronym already exists", DomainExceptionCode.DUPLICATE_FOUND);
                });

        space.setRoomName(dto.getName());
        space.setDescription(dto.getDescription());
        space.setCapacity(dto.getCapacity());
        space.setAcronym(dto.getAcronym());
        space.setStatus(SpaceStatus.AVAILABLE);

        this.academicSpaceRepository.save(space);

    }

    public  void changeSpaceStatus(String spaceId, String status) {
        var space = this.academicSpaceRepository.findAcademicSpaceById(UUID.fromString(spaceId))
                .orElseThrow(() -> new DomainException("Space not found"));

        space.setStatus(SpaceStatus.valueOf(status));

        this.academicSpaceRepository.save(space);
    }

    public Page<AcademicSpace> fetchSpacesPaginated(
            int page, int pageSize, String filterField, String filterValue) {

        PageRequest pageRequest = PageRequest.of(page, pageSize);

        if ("roomName".equalsIgnoreCase(filterField)) {
            return academicSpaceRepository.findByRoomNameContainingIgnoreCaseOrderByCreatedAtDesc(
                    filterValue, pageRequest);
        } else if ("acronym".equalsIgnoreCase(filterField)) {
            return academicSpaceRepository.findByAcronymContainingIgnoreCaseOrderByCreatedAtDesc(
                    filterValue, pageRequest);
        } else {
            return academicSpaceRepository.findAllByOrderByCreatedAtDesc(pageRequest);
        }
    }

    public List<AcademicSpace> fetchAllAvailableAcademicSpaces() {
        return this.academicSpaceRepository.findAllByStatus(SpaceStatus.AVAILABLE);
    }


    public List<AcademicSpace> fetchAllSpaces() {
        return this.academicSpaceRepository.findAll();
    }

}
