package com.example.simpleloginapi.user.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.simpleloginapi.user.dto.SignupRequestDto;
import com.example.simpleloginapi.user.dto.SignupResponseDto;
import com.example.simpleloginapi.user.service.AuthService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping
public class AuthController {
	private final AuthService authService;

	@PostMapping("/signup")
	public ResponseEntity<SignupResponseDto> signup(
		@RequestBody SignupRequestDto signupRequestDto
	) {
		return ResponseEntity.ok(authService.signup(signupRequestDto.getUsername(), signupRequestDto.getPassword(), signupRequestDto.getNickname()));
	}

}
