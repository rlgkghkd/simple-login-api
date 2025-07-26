package com.example.simpleloginapi.jwt.exceptions;

import com.example.simpleloginapi.common.exception.Errors;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum JwtError implements Errors {

	INVALID_TOKEN(401, "J001", "유효하지 않은 인증토큰입니다."),
	BAD_SIGNATURE(401, "J002", "JWT 서명이 올바르지 않습니다."),
	TOKEN_EXPIRED(401, "J003", "만료된 JWT 토큰입니다."),
	UNSUPPORTED_JWT_TOKEN(401, "J004", "지원하지 않는 JWT 토큰입니다.."),
	AUTHENTICATION_FAILED(401, "T005", "인증에 실패했습니다."),;

	private final int status;
	private final String internalCode;
	private final String message;
}
