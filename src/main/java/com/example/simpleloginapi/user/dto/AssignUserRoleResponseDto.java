package com.example.simpleloginapi.user.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class AssignUserRoleResponseDto {
	private String username;
	private String nickname;
	private List<RoleDto> roles;
}
