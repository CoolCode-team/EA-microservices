package user.user.domain.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import user.user.domain.entity.User;

public interface UserRepository extends JpaRepository<User, UUID> {
    Optional<User> findUserByEmail(String email);
    Optional<User> findUserById(UUID userId);

    Page<UserProjection> findAllBy(Pageable pageable);

    Page<UserProjection> findAllByOrderByUpdatedAtDesc(Pageable pageable);
    Page<UserProjection> findAllByOrderByUpdatedAtAsc(Pageable pageable);

    List<UserProjection> findAllBySchoolUnitId(UUID schoolUnitId);
}