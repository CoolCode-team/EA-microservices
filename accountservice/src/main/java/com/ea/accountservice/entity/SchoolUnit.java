package com.ea.accountservice.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Entity
@Data
public class SchoolUnit {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false, length = 255)
    private String name;

    @Column(nullable = false, length = 100)
    private String type;

    @Column(length = 100)
    private String state;

    @Column(length = 100)
    private String city;

    @Column(length = 255)
    private String address;

    @Column(name = "numero_contato", length = 15)
    private String contactNumber;

    @Column(name = "data_criacao")
    private LocalDateTime createdAt;

    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDateTime.now();
    }

    @JsonIgnore
    @OneToMany(mappedBy = "schoolUnit", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<User> users;

}
