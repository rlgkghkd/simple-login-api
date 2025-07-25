package com.example.simpleloginapi.user.service;

import org.springframework.stereotype.Service;

import com.example.simpleloginapi.user.dto.SignupResponseDto;
import com.example.simpleloginapi.user.entity.User;
import com.example.simpleloginapi.user.entity.UserRole;
import com.example.simpleloginapi.user.entity.UserRoles;
import com.example.simpleloginapi.user.repository.AuthRepository;
import com.example.simpleloginapi.user.repository.UserRolesRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthService {
	private final AuthRepository authRepository;
	private final UserRolesRepository userRolesRepository;

	public SignupResponseDto signup(String username, String password, String nickname) {
		User user = User.builder()
			.username(username)
			.nickname(nickname)
			.password(password)
			.build();

		UserRoles userRoles = UserRoles.builder()
			.user(user)
			.userRole(UserRole.USER)
			.build();

		user.addUserRole(userRoles);

		User saved = authRepository.save(user);
		userRolesRepository.save(userRoles);

		return new SignupResponseDto(saved.getUsername(), saved.getNickname(), saved.getUserRoles());
	}
}
