package com.example.reserva.infra.scripts;

import com.github.javafaker.Faker;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Random;
import java.util.UUID;

@Component
public class InsertSeedData implements CommandLineRunner {

  @Value("${spring.datasource.url}")
  private String JDBC_URL;

  @Value("${spring.datasource.username}")
  private String USER;

  @Value("${spring.datasource.password}")
  private String PASSWORD;

  @Value("${application.config.database.run_seed}")
  private String RUN_SEED;

  @Override
  public void run(String... args) throws Exception {

    System.out.println("Insert SeedData seed  " + RUN_SEED);
    System.out.println("Insert SeedData seed  " + JDBC_URL);
    System.out.println("Insert SeedData seed  " + USER);
    System.out.println("Insert SeedData seed  " + PASSWORD);

    
    var random = new Random();

    if (!Boolean.parseBoolean(RUN_SEED)) {
      return;
    }

    Faker faker = new Faker();
    BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    try (Connection conn = DriverManager.getConnection(JDBC_URL, USER, PASSWORD)) {



      // Insert 50 users
      String insertUserSQL =
              "INSERT INTO public.users (id, email, name, password_hash, role, contact_number, course, school_id) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

      // Create first admin user
      try {

        var stmt = conn.prepareStatement(insertUserSQL);

        UUID userId = UUID.randomUUID();
        stmt.setObject(1, userId);
        stmt.setString(2, "admin@admin.com");
        stmt.setString(3, faker.name().fullName());
        stmt.setString(4, encoder.encode("admin")); // Fake password
        stmt.setString(5, "ADMIN");
        stmt.setString(6, faker.phoneNumber().phoneNumber());
        stmt.setString(7, null);
        stmt.setObject(8, null);


        stmt.execute();

      } catch (RuntimeException e) {
        throw new RuntimeException(e);
      }

    } catch (SQLException e) {
      e.printStackTrace();
    }
  }
}