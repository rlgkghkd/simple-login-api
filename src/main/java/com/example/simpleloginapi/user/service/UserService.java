package com.example.simpleloginapi.user.service;

import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.example.simpleloginapi.user.entity.User;
import com.example.simpleloginapi.user.entity.UserRole;
import com.example.simpleloginapi.user.entity.UserRoleAssignment;
import com.example.simpleloginapi.user.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {
	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;

	public User createUser(String username, String password, String nickname) {
		if(userRepository.existsByUsername(username)) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
		}
		User user = User.builder()
			.username(username)
			.password(passwordEncoder.encode(password))
			.nickname(nickname)
			.build();

		createUserRoleAssignment(user, UserRole.USER);
		return userRepository.save(user);
	}

	public void createUserRoleAssignment(User user, UserRole userRole){
		UserRoleAssignment userRoleAssignment = UserRoleAssignment.builder()
			.user(user)
			.userRole(userRole)
			.build();

		user.addAssignment(userRoleAssignment);
	}
}
