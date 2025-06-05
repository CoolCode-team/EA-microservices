package com.ea.accountservice.service;

import com.ea.accountservice.dto.CreateSchoolUnitDto;
import com.ea.accountservice.entity.SchoolUnit;
import com.ea.accountservice.repository.SchoolUnitRepository;
import com.ea.accountservice.repository.UserProjection;
import com.ea.accountservice.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class SchoolUnitService
{

    private final SchoolUnitRepository repository;
    private final UserRepository userRepository;

    public SchoolUnitService(SchoolUnitRepository repository, UserRepository userRepository) {
        this.repository = repository;
        this.userRepository = userRepository;
    }

    public SchoolUnit create(CreateSchoolUnitDto dto) {
        SchoolUnit unit = new SchoolUnit();
        unit.setName(dto.name());
        unit.setType(dto.type());
        unit.setState(dto.state());
        unit.setCity(dto.city());
        unit.setAddress(dto.address());
        unit.setContactNumber(dto.contactNumber());

        return repository.save(unit);
    }

    public List<SchoolUnit> findAll() {
        return repository.findAll();
    }


    public List<UserProjection> getTeachersBySchoolUnit(UUID schoolUnitId) {
        return this.userRepository.findAllBySchoolUnit_Id(schoolUnitId);

    }


    public void delete(UUID schoolUnitId) {
        var unit = this.repository.findById(schoolUnitId)
                .orElseThrow(() -> new IllegalArgumentException("School unit not found"));
        this.repository.delete(unit);
    }
}
