package com.ea.accountservice.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;

import java.io.Serial;
import java.time.LocalDateTime;
import java.util.UUID;


@Data
@Entity(name = "users")
public class User {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id()
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;


    @Column(nullable = false)
    private String name;

    @Column(nullable = false, unique = true)
    private  String email;

    @JsonIgnore
    @Column(nullable = false, columnDefinition = "TEXT")
    private String passwordHash;

    @Column(nullable = true, columnDefinition = "TEXT")
    private String contactNumber;


    @Column(nullable = true, columnDefinition = "TEXT")
    private String course;


    @Column()
    private LocalDateTime updatedAt;


    @Enumerated(EnumType.STRING)
    @Column(nullable = false ,columnDefinition = "TEXT")
    private UserRole role;

    @ManyToOne
    @JoinColumn(name = "school_id", nullable = true)
    private SchoolUnit schoolUnit;

}
