package com.example.simpleloginapi.user.service;

import org.springframework.stereotype.Service;

import com.example.simpleloginapi.user.repository.LoginRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class LoginService {
	private final LoginRepository loginRepository;
}
