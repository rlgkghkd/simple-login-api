package com.example.simpleloginapi.user.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.simpleloginapi.common.exception.CustomException;
import com.example.simpleloginapi.user.entity.User;
import com.example.simpleloginapi.user.exception.UserErrors;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

	Optional<User> findByUsername(String username);
	default User findByUsernameOrElseThrow(String username) {
		return  findByUsername(username).orElseThrow(()-> new CustomException(UserErrors.USER_NOT_FOUND));
	}

	default User findByIdOrElseThrow(Long id){
		return findById(id).orElseThrow(()-> new CustomException(UserErrors.USER_NOT_FOUND));
	}

	Boolean existsByUsername(String username);
}
