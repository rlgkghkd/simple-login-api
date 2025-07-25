package com.example.simpleloginapi.user.controller;

import org.springframework.web.bind.annotation.RestController;

import com.example.simpleloginapi.user.service.LoginService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class LoginController {
	private final LoginService loginService;
}
