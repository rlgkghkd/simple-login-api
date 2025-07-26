package com.example.simpleloginapi.common.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum CommonErrors implements Errors{
	VALIDATION_FAILED(400, "V001", "요청 유효성 검사에 실패했습니다."),
	INVALID_REQUEST_BODY(400, "V002", "요청 본문 형식이 올바르지 않습니다.");

	private final int status;
	private final String internalCode;
	private final String message;
}
