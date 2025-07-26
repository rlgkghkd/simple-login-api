package com.example.simpleloginapi;

import org.junit.jupiter.api.Nested;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MvcResult;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import com.example.simpleloginapi.user.dto.LoginResponseDto;
import com.example.simpleloginapi.user.entity.User;
import com.example.simpleloginapi.user.entity.UserRole;
import com.example.simpleloginapi.user.entity.UserRoleAssignment;
import com.example.simpleloginapi.user.repository.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@Transactional
class SimpleLoginApiApplicationTests {

	@Autowired
	private MockMvc mockMvc;
	@Autowired
	private ObjectMapper objectMapper;
	@Autowired
	private PasswordEncoder passwordEncoder;
	@Autowired
	private UserRepository userRepository;

	@Test
	void contextLoads() {
	}

	@Nested
	class SignupTest {
		@Test
		void signup () throws Exception{
			String jsonContent = "{\"username\":\"testuser\", \"password\":\"Password2#\", \"nickname\":\"testnick\"}";
			mockMvc.perform(post("/signup")
					.contentType(MediaType.APPLICATION_JSON)
					.content(jsonContent)
				)
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.username").value("testuser"))
				.andExpect(jsonPath("$.nickname").value("testnick"))
				.andExpect(jsonPath("$.roles[0].role").value("USER"));
		}
		@Test
		void duplicateUsername () throws Exception{
			String jsonContent = "{\"username\":\"testuser\", \"password\":\"Password2#\", \"nickname\":\"testnick\"}";
			mockMvc.perform(post("/signup")
				.contentType(MediaType.APPLICATION_JSON)
				.content(jsonContent)
			);
			mockMvc.perform(post("/signup")
					.contentType(MediaType.APPLICATION_JSON)
					.content(jsonContent)
				)
				.andExpect(status().isConflict())
				.andExpect(jsonPath("$.error.status").value("409"))
				.andExpect(jsonPath("$.error.code").value("USER_ALREADY_EXISTS"))
				.andExpect(jsonPath("$.error.internalCode").value("U002"))
				.andExpect(jsonPath("$.error.message").value("이미 가입된 사용자입니다."));
		}
	}

	@Nested
	class LoginTest {
		@Test
		void login () throws Exception{
			String signupContent = "{\"username\":\"testuser\", \"password\":\"Password2#\", \"nickname\":\"testnick\"}";
			mockMvc.perform(post("/signup")
				.contentType(MediaType.APPLICATION_JSON)
				.content(signupContent)
			);
			String jsonContent = "{\"username\":\"testuser\", \"password\":\"Password2#\"}";
			mockMvc.perform(post("/login")
					.contentType(MediaType.APPLICATION_JSON)
					.content(jsonContent)
				)
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.token").exists());
		}

		@Test
		void wrongUsername () throws Exception{
			String signupContent = "{\"username\":\"testuser\", \"password\":\"Password2#\", \"nickname\":\"testnick\"}";
			mockMvc.perform(post("/signup")
				.contentType(MediaType.APPLICATION_JSON)
				.content(signupContent)
			);
			String jsonContent = "{\"username\":\"wrongName\", \"password\":\"Password2#\"}";
			mockMvc.perform(post("/login")
					.contentType(MediaType.APPLICATION_JSON)
					.content(jsonContent)
				)
				.andExpect(status().isNotFound())
				.andExpect(jsonPath("$.error.status").value("404"))
				.andExpect(jsonPath("$.error.code").value("USER_NOT_FOUND"))
				.andExpect(jsonPath("$.error.internalCode").value("U001"))
				.andExpect(jsonPath("$.error.message").value("존재하지 않는 유저입니다."));
		}

		@Test
		void wrongPassword () throws Exception{
			String signupContent = "{\"username\":\"testuser\", \"password\":\"Password2#\", \"nickname\":\"testnick\"}";
			mockMvc.perform(post("/signup")
				.contentType(MediaType.APPLICATION_JSON)
				.content(signupContent)
			);
			String jsonContent = "{\"username\":\"testuser\", \"password\":\"Password2##\"}";
			mockMvc.perform(post("/login")
					.contentType(MediaType.APPLICATION_JSON)
					.content(jsonContent)
				)
				.andExpect(status().isUnauthorized())
				.andExpect(jsonPath("$.error.status").value("401"))
				.andExpect(jsonPath("$.error.code").value("INVALID_CREDENTIALS"))
				.andExpect(jsonPath("$.error.internalCode").value("A002"))
				.andExpect(jsonPath("$.error.message").value("비밀번호가 올바르지 않습니다."));
		}
	}

	@Nested
	class RoleAssign {
		@Test
		void assignTest () throws Exception{
			String password = passwordEncoder.encode("Password2#");
			User admin = new User("testAdmin", password, "testnick");
			admin.addAssignment(new UserRoleAssignment(admin, UserRole.ADMIN));
			userRepository.save(admin);


			User user = new User("testUser", password, "testnick");
			user.addAssignment(new UserRoleAssignment(user, UserRole.USER));
			userRepository.save(user);
			Long userId = user.getId();

			// 로그인 해서 토큰 추출
			String jsonContent = "{\"username\":\"testAdmin\", \"password\":\"Password2#\"}";
			MvcResult loginResult = mockMvc.perform(post("/login")
				.contentType(MediaType.APPLICATION_JSON)
				.content(jsonContent)
			).andReturn();
			String responseBody = loginResult.getResponse().getContentAsString();
			LoginResponseDto loginResponseDto = objectMapper.readValue(responseBody, LoginResponseDto.class);
			String token = loginResponseDto.getToken();

			String patch = "{\"userRole\":\"ADMIN\"}";
			mockMvc.perform(patch("/admin/users/{$userId}/roles", userId)
					.header("Authorization", "Bearer " + token)
					.contentType(MediaType.APPLICATION_JSON)
					.content(patch)
				)
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.username").value("testUser"))
				.andExpect(jsonPath("$.nickname").value("testnick"))
				.andExpect(jsonPath("$.roles[0].role").value("USER"))
				.andExpect(jsonPath("$.roles[1].role").value("ADMIN"));
		}

		@Test
		void bad_role () throws Exception{
			String password = passwordEncoder.encode("Password2#");
			User admin = new User("testAdmin", password, "testnick");
			admin.addAssignment(new UserRoleAssignment(admin, UserRole.ADMIN));
			userRepository.save(admin);


			User user = new User("testUser", password, "testnick");
			user.addAssignment(new UserRoleAssignment(user, UserRole.USER));
			userRepository.save(user);
			Long userId = user.getId();

			// 로그인 해서 토큰 추출
			String jsonContent = "{\"username\":\"testAdmin\", \"password\":\"Password2#\"}";
			MvcResult loginResult = mockMvc.perform(post("/login")
				.contentType(MediaType.APPLICATION_JSON)
				.content(jsonContent)
			).andReturn();
			String responseBody = loginResult.getResponse().getContentAsString();
			LoginResponseDto loginResponseDto = objectMapper.readValue(responseBody, LoginResponseDto.class);
			String token = loginResponseDto.getToken();

			String patch = "{\"userRole\":\"SUPERADMIN\"}";
			mockMvc.perform(patch("/admin/users/{$userId}/roles", userId)
					.header("Authorization", "Bearer " + token)
					.contentType(MediaType.APPLICATION_JSON)
					.content(patch)
				)
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.error.status").value("400"))
				.andExpect(jsonPath("$.error.code").value("INVALID_REQUEST_BODY"))
				.andExpect(jsonPath("$.error.internalCode").value("V002"))
				.andExpect(jsonPath("$.error.message").value("요청 본문 형식이 올바르지 않습니다."));
		}

		@Test
		void user_can_not_assign_role () throws Exception{
			String password = passwordEncoder.encode("Password2#");
			User admin = new User("testAdmin", password, "testnick");
			admin.addAssignment(new UserRoleAssignment(admin, UserRole.ADMIN));
			userRepository.save(admin);


			User user = new User("testUser", password, "testnick");
			user.addAssignment(new UserRoleAssignment(user, UserRole.USER));
			userRepository.save(user);

			// 로그인 해서 토큰 추출
			String jsonContent = "{\"username\":\"testUser\", \"password\":\"Password2#\"}";
			MvcResult loginResult = mockMvc.perform(post("/login")
				.contentType(MediaType.APPLICATION_JSON)
				.content(jsonContent)
			).andReturn();
			String responseBody = loginResult.getResponse().getContentAsString();
			LoginResponseDto loginResponseDto = objectMapper.readValue(responseBody, LoginResponseDto.class);
			String token = loginResponseDto.getToken();

			String patch = "{\"userRole\":\"ADMIN\"}";
			mockMvc.perform(patch("/admin/users/2/roles")
					.header("Authorization", "Bearer " + token)
					.contentType(MediaType.APPLICATION_JSON)
					.content(patch)
				)
				.andExpect(status().isForbidden())
				.andExpect(jsonPath("$.error.status").value("403"))
				.andExpect(jsonPath("$.error.code").value("ACCESS_DENIED"))
				.andExpect(jsonPath("$.error.internalCode").value("A001"))
				.andExpect(jsonPath("$.error.message").value("접근 권한이 없습니다."));
		}

		@Test
		void user_not_found () throws Exception{
			String password = passwordEncoder.encode("Password2#");
			User admin = new User("testAdmin", password, "testnick");
			admin.addAssignment(new UserRoleAssignment(admin, UserRole.ADMIN));
			userRepository.save(admin);

			// 로그인 해서 토큰 추출
			String jsonContent = "{\"username\":\"testAdmin\", \"password\":\"Password2#\"}";
			MvcResult loginResult = mockMvc.perform(post("/login")
				.contentType(MediaType.APPLICATION_JSON)
				.content(jsonContent)
			).andReturn();
			String responseBody = loginResult.getResponse().getContentAsString();
			LoginResponseDto loginResponseDto = objectMapper.readValue(responseBody, LoginResponseDto.class);
			String token = loginResponseDto.getToken();

			String patch = "{\"userRole\":\"ADMIN\"}";
			mockMvc.perform(patch("/admin/users/{$userId}/roles", Long.MAX_VALUE)
					.header("Authorization", "Bearer " + token)
					.contentType(MediaType.APPLICATION_JSON)
					.content(patch)
				)
				.andExpect(status().isNotFound())
				.andExpect(jsonPath("$.error.status").value("404"))
				.andExpect(jsonPath("$.error.code").value("USER_NOT_FOUND"))
				.andExpect(jsonPath("$.error.internalCode").value("U001"))
				.andExpect(jsonPath("$.error.message").value("존재하지 않는 유저입니다."));
		}
	}
}
