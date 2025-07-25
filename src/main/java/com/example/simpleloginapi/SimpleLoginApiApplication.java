package com.example.simpleloginapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class SimpleLoginApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(SimpleLoginApiApplication.class, args);
	}

}
