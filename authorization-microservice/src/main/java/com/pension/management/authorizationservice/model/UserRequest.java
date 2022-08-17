package com.pension.management.authorizationservice.model;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import org.springframework.stereotype.Component;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Component
public class UserRequest {

	@NotBlank(message = "Cannot be empty")
	private String username;

	@NotBlank(message = "Cannot be empty")
	private String password;

	public UserRequest() {
		super();
	}

	public UserRequest(@NotBlank(message = "Cannot be empty") String username,
			@NotBlank(message = "Cannot be empty") String password) {
		super();
		this.username = username;
		this.password = password;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
}