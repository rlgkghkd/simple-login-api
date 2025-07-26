package com.example.simpleloginapi.user.exception;

import com.example.simpleloginapi.common.exception.Errors;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum AuthErrors implements Errors {

	ACCESS_DENIED(403, "A001", "접근 권한이 없습니다."),
	INVALID_CREDENTIALS(401, "A002", "비밀번호가 올바르지 않습니다."),;

	private final int status;
	private final String internalCode;
	private final String message;
}
