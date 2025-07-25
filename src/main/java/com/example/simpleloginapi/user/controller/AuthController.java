package com.example.simpleloginapi.user.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.simpleloginapi.user.dto.LoginRequestDto;
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
	public ResponseEntity<SignupResponseDto> signup(@RequestBody SignupRequestDto signupRequestDto) {
		return ResponseEntity.ok(authService.signup(signupRequestDto.getUsername(), signupRequestDto.getPassword(), signupRequestDto.getNickname()));
	}

	@PostMapping("/login")
	public ResponseEntity<Void> login(@RequestBody LoginRequestDto loginRequestDto) {
		authService.login(loginRequestDto.getUsername(), loginRequestDto.getPassword());
		return new ResponseEntity<>(HttpStatus.OK);
	}

	///  TODO: jwt 도입 이후 구현
	@GetMapping("/logout")
	public ResponseEntity<Void> logout(){
		return null;
	}
}
