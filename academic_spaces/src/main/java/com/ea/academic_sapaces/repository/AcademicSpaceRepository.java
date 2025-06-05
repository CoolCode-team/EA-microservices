package com.ea.academic_sapaces.repository;

import com.ea.academic_sapaces.entity.AcademicSpace;
import com.ea.academic_sapaces.entity.SpaceStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

public interface AcademicSpaceRepository  extends JpaRepository<AcademicSpace, UUID> {
    Optional<AcademicSpace> findAcademicSpaceByAcronym(String acronym);
    Optional<AcademicSpace> findAcademicSpaceById(UUID id);

    @Query(
            value =
                    "SELECT s.id AS academicSpaceId, s.acronym AS acronym, s.room_name AS roomName, COUNT(r.id) AS count "
                            + "FROM public.spaces s "
                            + "LEFT JOIN reservations r ON s.id = r.academic_space_id AND r.start_date_time >= NOW() - INTERVAL '7 days' "
                            + "GROUP BY s.id, s.acronym, s.room_name "
                            + "ORDER BY count DESC "
                            + "LIMIT 10",
            nativeQuery = true)
    List<Map<String, Object>> countReservationsByAcademicSpaceLast7Days();

    Page<AcademicSpace> findAllByOrderByCreatedAtDesc(PageRequest pageRequest);

    Page<AcademicSpace> findByRoomNameContainingIgnoreCaseOrderByCreatedAtDesc(
            String roomName, PageRequest pageRequest);

    Page<AcademicSpace> findByAcronymContainingIgnoreCaseOrderByCreatedAtDesc(
            String acronym, PageRequest pageRequest);

    List<AcademicSpace> findAllByStatus(SpaceStatus status);
}
