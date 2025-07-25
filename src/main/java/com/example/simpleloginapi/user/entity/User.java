package com.example.simpleloginapi.user.entity;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@Getter
public class User {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(unique = true)
	private String username;

	private String password;

	private String nickname;

	@OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<UserRoleAssignment> userRoles = new ArrayList<>();

	@Builder
	public User(String username, String password, String nickname, UserRoleAssignment userRoleAssignment) {
		this.username = username;
		this.password = password;
		this.nickname = nickname;
	}

	public void addAssignment(UserRoleAssignment userRoleAssignment) {
		this.userRoles.add(userRoleAssignment);
	}
}
