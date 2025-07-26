package com.example.simpleloginapi.common.exception;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(CustomException.class)
	public ResponseEntity<ErrorResponseDto> noTokenException(CustomException e) {
		Errors errors = e.getErrors();
		ErrorDetailDto detail = ErrorDetailDto.of(errors, e.getMessage());
		ErrorResponseDto response = new ErrorResponseDto(detail);
		return new ResponseEntity<>(response, HttpStatus.valueOf(e.getErrors().getStatus()));
	}

	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<ErrorResponseDto> handleValidationExceptions(MethodArgumentNotValidException e) {
		List<ErrorDetailDto.FieldError> fieldErrors = e.getBindingResult().getFieldErrors().stream()
			.map(error -> ErrorDetailDto.FieldError.of(
				error.getField(),
				error.getRejectedValue() != null ? error.getRejectedValue().toString() : "",
				error.getDefaultMessage()
			))
			.toList();

		Errors validationError = CommonErrors.VALIDATION_FAILED;
		ErrorDetailDto detailDto = ErrorDetailDto.of(validationError, fieldErrors);
		ErrorResponseDto responseDto = new ErrorResponseDto(detailDto);

		return new ResponseEntity<>(responseDto, HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(HttpMessageNotReadableException.class)
	public ResponseEntity<ErrorResponseDto> handleIllegalArgumentException(HttpMessageNotReadableException e) {

		Errors validationError = CommonErrors.INVALID_REQUEST_BODY;
		String errorMessage = validationError.getMessage();

		Throwable cause = e.getCause();
		if (cause instanceof IllegalArgumentException) {
			errorMessage = cause.getMessage();
		}

		ErrorDetailDto detailDto = ErrorDetailDto.of(validationError, errorMessage);
		ErrorResponseDto responseDto = new ErrorResponseDto(detailDto);
		return new ResponseEntity<>(responseDto, HttpStatus.BAD_REQUEST);
	}
}
