package com.example.simpleloginapi.user.dto;

import com.example.simpleloginapi.user.entity.UserRole;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class RoleDto {
	private UserRole role;
}
