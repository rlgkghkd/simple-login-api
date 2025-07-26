package com.example.simpleloginapi.user.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class SignupRequestDto {
	@NotBlank(message = "사용자 이름은 필수입니다.")
	@Size(min = 4, max = 12, message = "4자 이상 12자 이하여야 합니다.")
	private String username;

	@NotBlank(message = "비밀번호는 필수입니다.")
	@Size(min = 8, message = "8자 이상 이어야 합니다.")
	@Pattern(regexp = "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>/?]).{8,}$", message = "비밀번호는 최소 8자, 대문자, 소문자, 숫자, 특수문자를 각각 1개 이상 포함해야 합니다.")
	private String password;

	@NotBlank(message = "닉네임은 필수입니다.")
	@Size(max = 24, message = "24자 이하 이어야 합니다.")
	private String nickname;
}
