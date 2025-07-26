package com.example.simpleloginapi.user.service;

import java.util.ArrayList;
import java.util.List;

import javax.management.relation.Role;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.example.simpleloginapi.common.exception.CustomException;
import com.example.simpleloginapi.user.dto.AssignUserRoleResponseDto;
import com.example.simpleloginapi.user.dto.RoleDto;
import com.example.simpleloginapi.user.entity.User;
import com.example.simpleloginapi.user.entity.UserDetailImpl;
import com.example.simpleloginapi.user.entity.UserRole;
import com.example.simpleloginapi.user.entity.UserRoleAssignment;
import com.example.simpleloginapi.user.exception.AuthErrors;
import com.example.simpleloginapi.user.exception.UserErrors;
import com.example.simpleloginapi.user.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {
	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;

	public User createUser(String username, String password, String nickname) {
		if(userRepository.existsByUsername(username)) {
			throw new CustomException(UserErrors.USER_ALREADY_EXISTS);
		}
		User user = User.builder()
			.username(username)
			.password(passwordEncoder.encode(password))
			.nickname(nickname)
			.build();

		createUserRoleAssignment(user, UserRole.USER);
		return userRepository.save(user);
	}

	public AssignUserRoleResponseDto assignUserRole(Long userId, UserRole userRole) {
		User user = userRepository.findByIdOrElseThrow(userId);
		createUserRoleAssignment(user, userRole);

		List<RoleDto> roleDtos = new ArrayList<>();
		for (UserRoleAssignment assignment : user.getUserRoleAssignments()){
			roleDtos.add(new RoleDto(assignment.getUserRole()));
		}
		return new AssignUserRoleResponseDto(user.getUsername(), user.getNickname(), roleDtos);
	}

	public void createUserRoleAssignment(User user, UserRole userRole){
		UserRoleAssignment userRoleAssignment = UserRoleAssignment.builder()
			.user(user)
			.userRole(userRole)
			.build();

		user.addAssignment(userRoleAssignment);
	}
}
