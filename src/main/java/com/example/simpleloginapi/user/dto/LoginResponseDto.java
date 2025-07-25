package com.example.simpleloginapi.user.dto;

import java.util.List;

import com.example.simpleloginapi.user.entity.UserRoles;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
public class LoginResponseDto {
	private String username;
	private String nickname;
	private List<UserRoles> roles;
}
