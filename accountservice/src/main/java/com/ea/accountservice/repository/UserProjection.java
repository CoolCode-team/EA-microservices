package com.ea.accountservice.repository;

import com.ea.accountservice.entity.SchoolUnit;
import com.ea.accountservice.entity.UserRole;


import java.util.UUID;

public interface UserProjection {
  UUID getId();

  String getName();

  String getEmail();

  UserRole getRole();

  String getContactNumber();

  String getCourse();

  SchoolUnit getSchoolUnit();
}
