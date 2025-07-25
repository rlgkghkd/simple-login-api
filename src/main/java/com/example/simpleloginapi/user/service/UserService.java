package com.example.simpleloginapi.user.service;

import org.springframework.stereotype.Service;

import com.example.simpleloginapi.user.entity.User;
import com.example.simpleloginapi.user.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {
	private final UserRepository userRepository;

	public User saveUser (String username, String password, String nickname) {
		User user = User.builder()
			.username(username)
			.password(password)
			.nickname(nickname)
			.build();

		return userRepository.save(user);
	}
}
