package com.example.simpleloginapi;

import com.example.simpleloginapi.user.entity.User;
import com.example.simpleloginapi.user.entity.UserRole;
import com.example.simpleloginapi.user.entity.UserRoleAssignment;
import com.example.simpleloginapi.user.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
@EnableJpaAuditing
public class SimpleLoginApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(SimpleLoginApiApplication.class, args);
	}

	@Bean
	public CommandLineRunner initAdminUser(UserRepository userRepository, PasswordEncoder passwordEncoder) {
		return args -> {
			// 관리자 계정 존재 여부 확인
			if (userRepository.findByUsername("admin").isEmpty()) {
				// 관리자 계정 생성
				User adminUser = User.builder()
						.username("admin")
						.password(passwordEncoder.encode("Admin12#$")) // 안전한 비밀번호로 변경 권장
						.nickname("Admin")
						.build();
				UserRoleAssignment userRoleAssignment = new UserRoleAssignment(adminUser, UserRole.ADMIN);
				adminUser.addAssignment(userRoleAssignment);
				userRepository.save(adminUser);
				System.out.println("Admin user 'admin' created successfully.");
			} else {
				System.out.println("Admin user 'admin' already exists.");
			}
		};
	}
}
