package com.ea.accountservice.service;


import com.ea.accountservice.dto.CreateUserDto;
import com.ea.accountservice.dto.UpdateUserDto;
import com.ea.accountservice.entity.User;
import com.ea.accountservice.entity.UserRole;
import com.ea.accountservice.infra.DomainException;
import com.ea.accountservice.repository.SchoolUnitRepository;
import com.ea.accountservice.repository.UserProjection;
import com.ea.accountservice.repository.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder hashService;
    private final SchoolUnitRepository schoolUnitRepository;

    public UserService(UserRepository userRepository, PasswordEncoder hashService, SchoolUnitRepository schoolUnitRepository) {
        this.userRepository = userRepository;
        this.hashService = hashService;
        this.schoolUnitRepository = schoolUnitRepository;

    }


    public void createAdminUser(CreateUserDto createUserDto) {
        User user = new User();

        var existUser = this.userRepository.findUserByEmail((createUserDto.getEmail()));

        if(existUser.isPresent()) {
            throw new DomainException("User already exists");
        }

        user.setEmail(createUserDto.getEmail());
        user.setName(createUserDto.getName());
        user.setContactNumber(createUserDto.getContactNumber());
        user.setPasswordHash(this.hashService.encode(createUserDto.getPassword()));
        user.setRole(UserRole.ADMIN);

        this.userRepository.save(user);
    }


    public void createTeacher(CreateUserDto createUserDto) {
        User user = new User();

        var existUser = this.userRepository.findUserByEmail((createUserDto.getEmail()));

        if (existUser.isPresent()) {
            throw new DomainException("User already exists");
        }


        user.setEmail(createUserDto.getEmail());
        user.setName(createUserDto.getName());
        user.setPasswordHash(this.hashService.encode(createUserDto.getPassword()));
        user.setRole(UserRole.TEACHER);
        user.setCourse(createUserDto.getCourse());
        user.setContactNumber(createUserDto.getContactNumber());


        if (createUserDto.getSchoolUnitId() == null) {
            throw new DomainException("School unit  is  required for teacher");
        }

        var schoolUnit = this.schoolUnitRepository.findById(
                UUID.fromString(createUserDto.getSchoolUnitId())
        );

        if (schoolUnit.isEmpty()) {
            throw new DomainException("School unit not found");
        }

        user.setSchoolUnit(schoolUnit.get());

        this.userRepository.save(user);

    }




    public User findByEmail(String email){
        var  user = this.userRepository.findUserByEmail(email);

        return user.orElse(null);
    }

    public Page<UserProjection> fetchUsersPaginated(int page, int pageSize) {
        return this.userRepository.findAllByOrderByUpdatedAtAsc(PageRequest.of(page, pageSize));
    }

    public void updateUser(String userId, UpdateUserDto dto) {
        var user =
                this.userRepository
                        .findUserById(UUID.fromString(userId))
                        .orElseThrow(() -> new DomainException("user not found"));

        user.setEmail(!dto.email().isEmpty() ? dto.email() : user.getEmail());
        user.setName(!dto.name().isEmpty() ? dto.name() : user.getName());
        user.setRole(!dto.role().isEmpty() ? UserRole.valueOf(dto.role()) : user.getRole());

        this.userRepository.save(user);
    }


    public void deleteUser(String userId) {
        var user = this.userRepository.findUserById(UUID.fromString(userId))
                .orElseThrow(() -> new DomainException("User not found"));

        this.userRepository.delete(user);
    }

}
