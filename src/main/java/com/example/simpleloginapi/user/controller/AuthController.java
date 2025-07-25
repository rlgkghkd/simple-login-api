package com.example.simpleloginapi.user.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.simpleloginapi.user.dto.LoginRequestDto;
import com.example.simpleloginapi.user.dto.LoginResponseDto;
import com.example.simpleloginapi.user.service.AuthService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping
public class AuthController {
	private final AuthService authService;

	@PostMapping("/signup")
	public ResponseEntity<LoginResponseDto> signup(
		@RequestBody LoginRequestDto loginRequestDto
	) {
		return ResponseEntity.ok(authService.signup(loginRequestDto.getUsername(), loginRequestDto.getPassword(), loginRequestDto.getNickname()));
	}

}
