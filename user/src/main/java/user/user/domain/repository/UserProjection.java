package user.user.domain.repository;

import java.util.UUID;

import user.user.domain.entity.UserRole;

public interface UserProjection {
 UUID getId();

 String getName();

 String getEmail();

 UserRole getRole();

 String getContactNumber();

 String getCourse();

 UUID getSchoolUnitId(); 
}
