package user.user.domain.entity;


import java.io.Serial;
import java.io.Serializable;
import java.util.UUID; // Assuming SchoolUnit ID is also UUID

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import user.user.shared.DomainEntity;
 

@Entity(name = "users") 
@Table(name = "users") 
@Getter
@Setter
public class User extends DomainEntity implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, unique = true)
    private String email;

    @com.fasterxml.jackson.annotation.JsonIgnore 
    @Column(nullable = false, columnDefinition = "TEXT") 
    private String passwordHash;

    @Column(nullable = true) 
    private String contactNumber;

    @Column(nullable = true) 
    private String course;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false) 
    private UserRole role;

    @Column(name = "school_unit_id", nullable = true) 
    private UUID schoolUnitId; 
}
