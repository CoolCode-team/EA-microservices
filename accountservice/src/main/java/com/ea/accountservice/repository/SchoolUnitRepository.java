package com.ea.accountservice.repository;

import com.ea.accountservice.entity.SchoolUnit;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface SchoolUnitRepository  extends JpaRepository<SchoolUnit, UUID> {
}
