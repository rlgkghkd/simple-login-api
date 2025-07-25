package com.example.simpleloginapi.user.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class SignupRequestDto {
	private String username;
	private String password;
	private String nickname;
}
