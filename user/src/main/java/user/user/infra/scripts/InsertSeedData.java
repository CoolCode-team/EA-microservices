package user.user.infra.scripts;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order; // Para controlar a ordem de execução se houver múltiplos CommandLineRunner
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@Order(1) // Define a ordem de execução, útil se tiver outros runners
public class InsertSeedData implements CommandLineRunner {

    private static final Logger logger = LoggerFactory.getLogger(InsertSeedData.class);

    @Value("${spring.datasource.url}")
    private String jdbcUrl;

    @Value("${spring.datasource.username}")
    private String dbUser;

    @Value("${spring.datasource.password}")
    private String dbPassword;

    @Value("${application.config.database.run_seed:false}") // Default to false if not set
    private boolean runSeed;

    @Override
    public void run(String... args) {
        logger.info("Run seed data configuration: {}", runSeed);
        if (!runSeed) {
            logger.info("Skipping seed data insertion.");
            return;
        }

        logger.debug("JDBC URL: {}", jdbcUrl);
        logger.debug("DB User: {}", dbUser);
        // Não logar a senha, mesmo em debug, por segurança.

        Faker faker = new Faker();
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

        String insertUserSQL = "INSERT INTO public.users " +
                               "(id, email, name, password_hash, role, contact_number, course, school_id, created_at, updated_at) " +
                               "VALUES (?, ?, ?, ?, ?, ?, ?, ?, NOW(), NOW()) " + // Adiciona created_at e updated_at se sua DomainEntity os gerencia e a tabela os tem
                               "ON CONFLICT (email) DO NOTHING"; // Evita erro se o email já existir

        try (Connection conn = DriverManager.getConnection(jdbcUrl, dbUser, dbPassword);
             PreparedStatement stmt = conn.prepareStatement(insertUserSQL)) {

            logger.info("Attempting to insert seed admin user...");

            UUID userId = UUID.randomUUID();
            stmt.setObject(1, userId);
            stmt.setString(2, "admin@admin.com");
            stmt.setString(3, "Admin User"); // Nome fixo ou faker
            stmt.setString(4, encoder.encode("admin123")); // Senha de seed
            stmt.setString(5, "ADMIN");
            stmt.setString(6, faker.phoneNumber().cellPhone()); // Usar cellPhone para formatos mais comuns
            stmt.setString(7, null); // Course for admin
            stmt.setObject(8, null); // school_id for admin

            int affectedRows = stmt.executeUpdate();
            if (affectedRows > 0) {
                logger.info("Admin user seeded successfully with ID: {}", userId);
            } else {
                logger.info("Admin user admin@admin.com likely already exists. No new admin seeded.");
            }

            // Aqui você poderia adicionar um loop para criar mais usuários (ex: TEACHERs)
            // Exemplo para um professor:
            /*
            UUID teacherId = UUID.randomUUID();
            stmt.setObject(1, teacherId);
            stmt.setString(2, "teacher@example.com");
            stmt.setString(3, faker.name().fullName());
            stmt.setString(4, encoder.encode("teacher123"));
            stmt.setString(5, "TEACHER");
            stmt.setString(6, faker.phoneNumber().cellPhone());
            stmt.setString(7, faker.educator().course());
            stmt.setObject(8, UUID.randomUUID()); // Um UUID falso para school_id do professor
            affectedRows = stmt.executeUpdate();
            if (affectedRows > 0) {
                logger.info("Teacher user seeded successfully with ID: {}", teacherId);
            } else {
                logger.info("Teacher user teacher@example.com likely already exists.");
            }
            */

        } catch (SQLException e) {
            logger.error("SQL error during seed data insertion: {}", e.getMessage(), e);
        } catch (Exception e) {
            logger.error("Unexpected error during seed data insertion: {}", e.getMessage(), e);
        }
    }
}