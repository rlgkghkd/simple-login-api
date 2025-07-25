package com.example.simpleloginapi.user.dto;

import java.util.List;

import com.example.simpleloginapi.user.entity.UserRoleAssignment;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
public class SignupResponseDto {
	private String username;
	private String nickname;
	private List<UserRoleAssignment> roles;
}
