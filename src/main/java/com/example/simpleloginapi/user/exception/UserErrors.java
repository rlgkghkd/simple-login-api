package com.example.simpleloginapi.user.exception;

import com.example.simpleloginapi.common.exception.Errors;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum UserErrors implements Errors {

	USER_NOT_FOUND(404, "U001", "존재하지 않는 유저입니다."),
	USER_ALREADY_EXISTS(409, "U002", "이미 가입된 사용자입니다.");

	private final int status;
	private final String internalCode;
	private final String message;
}
