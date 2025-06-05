package com.ea.accountservice.repository;

import com.ea.accountservice.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {
    Optional<User> findUserByEmail(String email);
    Optional<User> findUserById(UUID userId);

    Page<UserProjection> findAllBy(Pageable pageable);

    Page<UserProjection> findAllByOrderByUpdatedAtDesc(PageRequest of);

    Page<UserProjection> findAllByOrderByUpdatedAtAsc(PageRequest of);

    List<UserProjection> findAllBySchoolUnit_Id(UUID schoolUnitId);
}
