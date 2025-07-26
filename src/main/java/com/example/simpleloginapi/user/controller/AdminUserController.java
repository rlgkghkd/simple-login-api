package com.example.simpleloginapi.user.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.simpleloginapi.common.exception.CustomException;
import com.example.simpleloginapi.common.exception.ErrorResponseDto;
import com.example.simpleloginapi.user.dto.AssignUserRoleRequestDto;
import com.example.simpleloginapi.user.dto.AssignUserRoleResponseDto;
import com.example.simpleloginapi.user.dto.SignupResponseDto;
import com.example.simpleloginapi.user.entity.UserDetailImpl;
import com.example.simpleloginapi.user.exception.AuthErrors;
import com.example.simpleloginapi.user.service.UserService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/users")
public class AdminUserController {

	private final UserService userService;

	@Operation(summary = "권한 부여", description = "등록된 사용자에 권한을 부여합니다..")
	@Parameter(name = "userId", description = "권한을 부여할 사용자의 Id", required = true)
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "권한 부여 성공.", content = @Content(schema = @Schema(implementation = AssignUserRoleResponseDto.class))),
		@ApiResponse(responseCode = "403", description = "접근 권한이 없는 사용자", content = @Content(schema = @Schema(implementation = ErrorResponseDto.class))),
		@ApiResponse(responseCode = "404", description = "존재하지 않는 유저", content = @Content(schema = @Schema(implementation = ErrorResponseDto.class))),
	})
	@PatchMapping("/{userId}/roles")
	public ResponseEntity<AssignUserRoleResponseDto> assignRole (@PathVariable Long userId, @RequestBody @Valid AssignUserRoleRequestDto request
		, @AuthenticationPrincipal UserDetailImpl userDetail){
		boolean isAdmin = userDetail.getAuthorities().stream().anyMatch(grantedAuthority -> grantedAuthority.toString().equals("ROLE_ADMIN"));
		if (!isAdmin){
			throw new CustomException(AuthErrors.ACCESS_DENIED);
		}
		return ResponseEntity.ok(userService.assignUserRole(userId, request.getUserRole()));
	}
}
