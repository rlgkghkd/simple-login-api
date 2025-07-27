package com.example.simpleloginapi.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.simpleloginapi.user.entity.User;
import com.example.simpleloginapi.user.entity.UserRole;
import com.example.simpleloginapi.user.entity.UserRoleAssignment;

public interface UserRolesAssignmentRepository extends JpaRepository<UserRoleAssignment, Long> {
	Boolean existsByUserAndUserRole(User user, UserRole userRole);
}
