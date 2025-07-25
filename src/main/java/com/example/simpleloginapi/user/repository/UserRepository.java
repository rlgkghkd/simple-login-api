package com.example.simpleloginapi.user.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Repository;
import org.springframework.web.server.ResponseStatusException;

import com.example.simpleloginapi.user.entity.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
	Optional<User> findByUsername(String username);
	default User findByUsernameOrElseThrow(String username) {
		return  findByUsername(username).orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND));
	}

	Boolean existsByUsername(String username);
}
