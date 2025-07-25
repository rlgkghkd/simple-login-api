package com.example.simpleloginapi.user.service;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import com.example.simpleloginapi.user.dto.SignupResponseDto;
import com.example.simpleloginapi.user.entity.User;
import com.example.simpleloginapi.user.repository.UserRepository;
import com.example.simpleloginapi.user.repository.UserRolesAssignmentRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class AuthService {
	private final UserRepository userRepository;
	private final UserService userService;
	private final UserRolesAssignmentRepository userRolesAssignmentRepository;

	public SignupResponseDto signup(String username, String password, String nickname) {

		User user = userService.createUser(username, password, nickname);
		return new SignupResponseDto(user.getUsername(), user.getNickname(), user.getUserRoles());
	}

	public void login(String username, String password) {
		User user = userRepository.findByUsernameOrElseThrow(username);
		if (!user.getPassword().equals(password)) {
			throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
		}
		// jwt 도입하면 토큰 리턴
	}
}
