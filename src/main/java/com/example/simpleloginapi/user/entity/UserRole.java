package com.example.simpleloginapi.user.entity;

import java.util.Arrays;

import org.springframework.security.core.GrantedAuthority;

public enum UserRole implements GrantedAuthority {
	USER, ADMIN;

	public static UserRole of(String role) {
		return Arrays.stream(UserRole.values())
			.filter(r -> r.name().equalsIgnoreCase(role))
			.findFirst()
			.orElseThrow();
	}

	@Override
	public String getAuthority() {
		return "ROLE_" + this.name();
	}
}
