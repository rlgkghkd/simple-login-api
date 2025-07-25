package com.example.simpleloginapi.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.simpleloginapi.user.entity.UserRoles;

public interface UserRolesRepository extends JpaRepository<UserRoles, Long> {
}
