package com.ea.academic_sapaces.entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.OffsetDateTime;
import java.util.UUID;


@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity(name = "spaces")
public class AcademicSpace {

    @Id
    @GeneratedValue(strategy =  GenerationType.UUID)
    protected UUID id;

    @Column(nullable = false, name = "room_name")
    private String  roomName;

    @Column(nullable = false, unique = true)
    private String acronym;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String description;

    @Column(nullable = false)
    private int capacity;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false ,columnDefinition = "VARCHAR(255) DEFAULT 'AVAILABLE'")
    private SpaceStatus status;

    @Column(nullable = false, columnDefinition = "TIMESTAMP WITH TIME ZONE")
    @CreationTimestamp
    private OffsetDateTime createdAt;

    public Boolean isAvailable() {
        return this.status.equals(SpaceStatus.AVAILABLE);
    }



}
