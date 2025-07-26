package com.example.simpleloginapi.user.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.simpleloginapi.common.exception.ErrorResponseDto;
import com.example.simpleloginapi.user.dto.LoginRequestDto;
import com.example.simpleloginapi.user.dto.LoginResponseDto;
import com.example.simpleloginapi.user.dto.SignupRequestDto;
import com.example.simpleloginapi.user.dto.SignupResponseDto;
import com.example.simpleloginapi.user.service.AuthService;

import io.swagger.v3.oas.annotations.Operation;

import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping
public class AuthController {
	private final AuthService authService;

	@Operation(summary = "회원가입", description = "새로운 사용자를 등록합니다.")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "회원가입 성공", content = @Content(schema = @Schema(implementation = SignupResponseDto.class))),
		@ApiResponse(responseCode = "400", description = "요청 데이터 유효성 검사 실패", content = @Content(schema = @Schema(implementation = ErrorResponseDto.class))),
		@ApiResponse(responseCode = "409", description = "user name 중복", content = @Content(schema = @Schema(implementation = ErrorResponseDto.class))),
	})
	@PostMapping("/signup")
	public ResponseEntity<SignupResponseDto> signup(@RequestBody @Valid SignupRequestDto signupRequestDto) {
		return ResponseEntity.ok(authService.signup(signupRequestDto.getUsername(), signupRequestDto.getPassword(), signupRequestDto.getNickname()));
	}

	@Operation(summary = "로그인", description = "등록된 사용자 계정으로 로그인 합니다..")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "로그인 성공", content = @Content(schema = @Schema(implementation = LoginResponseDto.class))),
		@ApiResponse(responseCode = "400", description = "요청 데이터 유효성 검사 실패", content = @Content(schema = @Schema(implementation = ErrorResponseDto.class))),
		@ApiResponse(responseCode = "401", description = "잘못된 비밀번호", content = @Content(schema = @Schema(implementation = ErrorResponseDto.class))),
		@ApiResponse(responseCode = "404", description = "존재하지 않는 유저", content = @Content(schema = @Schema(implementation = ErrorResponseDto.class))),
	})
	@PostMapping("/login")
	public ResponseEntity<LoginResponseDto> login(@RequestBody @Valid LoginRequestDto loginRequestDto) {
		authService.login(loginRequestDto.getUsername(), loginRequestDto.getPassword());
		return ResponseEntity.ok(authService.login(loginRequestDto.getUsername(), loginRequestDto.getPassword()));
	}
}
