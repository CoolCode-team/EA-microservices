package com.example.reserva.shared;
import java.time.LocalDateTime;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;

@MappedSuperclass
public class DomainEntity {
  @CreatedDate
  @Column(name = "created_at", columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
  protected LocalDateTime createdAt;

  @LastModifiedDate
  @Column(nullable = true, name = "updated_at")
  protected LocalDateTime updatedAt;

  @PrePersist
  protected void onCreate() {
    createdAt = LocalDateTime.now();
  }

  @PreUpdate
  protected void onUpdate() {
    System.out.println(" Updatee usr " + LocalDateTime.now());
    updatedAt = LocalDateTime.now();
  }
}
