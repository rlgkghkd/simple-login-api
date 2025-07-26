package com.example.simpleloginapi.user.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import com.example.simpleloginapi.common.exception.CustomException;
import com.example.simpleloginapi.jwt.utils.JwtUtil;
import com.example.simpleloginapi.user.dto.LoginResponseDto;
import com.example.simpleloginapi.user.dto.RoleDto;
import com.example.simpleloginapi.user.dto.SignupResponseDto;
import com.example.simpleloginapi.user.entity.User;
import com.example.simpleloginapi.user.entity.UserRoleAssignment;
import com.example.simpleloginapi.user.exception.AuthErrors;
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
	private final PasswordEncoder passwordEncoder;
	private final JwtUtil jwtUtil;

	public SignupResponseDto signup(String username, String password, String nickname) {
		User user = userService.createUser(username, password, nickname);

		List<RoleDto> roleDtos = new ArrayList<>();
		for (UserRoleAssignment assignment : user.getUserRoleAssignments()){
			roleDtos.add(new RoleDto(assignment.getUserRole()));
		}
		return new SignupResponseDto(user.getUsername(), user.getNickname(), roleDtos);
	}

	public LoginResponseDto login(String username, String password) {
		User user = userRepository.findByUsernameOrElseThrow(username);
		if (!passwordEncoder.matches(password, user.getPassword())) {
			throw new CustomException(AuthErrors.INVALID_CREDENTIALS);
		}
		String token = jwtUtil.createToken(user.getUsername(), user.getUserRoleAssignments());
		return new LoginResponseDto(token);
	}
}
