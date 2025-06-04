package user.user.domain.service;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import user.user.domain.dto.CreateUserDto;
import user.user.domain.dto.UpdateUserDto;
import user.user.domain.entity.User;
import user.user.domain.entity.UserRole;
import user.user.domain.repository.UserProjection;
import user.user.domain.repository.UserRepository;
import user.user.shared.DomainException;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public User createAdminUser(CreateUserDto createUserDto) {
        userRepository.findUserByEmail(createUserDto.getEmail()).ifPresent(u -> {
            throw new DomainException("User already exists with email: " + createUserDto.getEmail());
        });

        User user = new User();
        user.setEmail(createUserDto.getEmail());
        user.setName(createUserDto.getName());
        user.setContactNumber(createUserDto.getContactNumber());
        user.setPasswordHash(this.passwordEncoder.encode(createUserDto.getPassword()));
        user.setRole(UserRole.ADMIN);
        if (createUserDto.getSchoolUnitId() != null && !createUserDto.getSchoolUnitId().isBlank()) {
             try {
                user.setSchoolUnitId(UUID.fromString(createUserDto.getSchoolUnitId()));
            } catch (IllegalArgumentException e) {
                throw new DomainException("Invalid School Unit ID format for admin.");
            }
        }
        return this.userRepository.save(user);
    }

    @Transactional
    public User createTeacher(CreateUserDto createUserDto) {
        userRepository.findUserByEmail(createUserDto.getEmail()).ifPresent(u -> {
            throw new DomainException("User already exists with email: " + createUserDto.getEmail());
        });

        if (createUserDto.getSchoolUnitId() == null || createUserDto.getSchoolUnitId().isBlank()) {
            throw new DomainException("School unit ID is required for teacher");
        }

        UUID schoolUnitId;
        try {
            schoolUnitId = UUID.fromString(createUserDto.getSchoolUnitId());
        } catch (IllegalArgumentException e) {
            throw new DomainException("Invalid School Unit ID format.");
        }

        User user = new User();
        user.setEmail(createUserDto.getEmail());
        user.setName(createUserDto.getName());
        user.setPasswordHash(this.passwordEncoder.encode(createUserDto.getPassword()));
        user.setRole(UserRole.TEACHER);
        user.setCourse(createUserDto.getCourse());
        user.setContactNumber(createUserDto.getContactNumber());
        user.setSchoolUnitId(schoolUnitId);

        return this.userRepository.save(user);
    }

    public User findByEmail(String email) {
        return this.userRepository.findUserByEmail(email)
                .orElseThrow(() -> new DomainException("User not found with email: " + email));
    }

    public User findById(UUID userId) {
        return this.userRepository.findUserById(userId)
                .orElseThrow(() -> new DomainException("User not found with ID: " + userId));
    }

    public Page<UserProjection> fetchUsersPaginated(int page, int pageSize) {
        return this.userRepository.findAllBy(PageRequest.of(page, pageSize));
    }

    @Transactional
    public User updateUser(String userIdString, UpdateUserDto dto) {
        UUID userId = UUID.fromString(userIdString);
        User user = this.userRepository
                .findUserById(userId)
                .orElseThrow(() -> new DomainException("User not found with ID: " + userId));

        if (dto.email() != null && !dto.email().isBlank() && !dto.email().equals(user.getEmail())) {
            userRepository.findUserByEmail(dto.email()).ifPresent(existingUser -> {
                if (!existingUser.getId().equals(user.getId())) {
                    throw new DomainException("Email " + dto.email() + " is already in use.");
                }
            });
            user.setEmail(dto.email());
        }

        if (dto.name() != null && !dto.name().isBlank()) {
            user.setName(dto.name());
        }

        if (dto.role() != null && !dto.role().isBlank()) {
            try {
                user.setRole(UserRole.valueOf(dto.role().toUpperCase()));
            } catch (IllegalArgumentException e) {
                throw new DomainException("Invalid role value: " + dto.role());
            }
        }
        
        if (dto.contactNumber() != null) {
            user.setContactNumber(dto.contactNumber());
        }

        if (dto.course() != null) {
            user.setCourse(dto.course());
        }
        
        if (dto.schoolUnitId() != null && !dto.schoolUnitId().isBlank()) {
            if (user.getRole() == UserRole.TEACHER || user.getRole() == UserRole.ADMIN) {
                 try {
                    user.setSchoolUnitId(UUID.fromString(dto.schoolUnitId()));
                } catch (IllegalArgumentException e) {
                    throw new DomainException("Invalid School Unit ID format for update.");
                }
            }
        } else if (dto.schoolUnitId() != null && dto.schoolUnitId().isBlank()) { // Allow clearing schoolUnitId if applicable
             if (user.getRole() == UserRole.TEACHER || user.getRole() == UserRole.ADMIN) { // Or specific roles that can have it cleared
                user.setSchoolUnitId(null);
             }
        }

        return this.userRepository.save(user);
    }

    @Transactional
    public void deleteUser(String userIdString) {
        UUID userId = UUID.fromString(userIdString);
        User user = this.userRepository.findUserById(userId)
                .orElseThrow(() -> new DomainException("User not found with ID: " + userId));
        this.userRepository.delete(user);
    }
}
