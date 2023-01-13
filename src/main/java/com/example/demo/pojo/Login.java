	package com.example.demo.pojo;

import javax.validation.constraints.NotBlank;

import lombok.Data;

@Data
public class Login {
	@NotBlank(message="username cannot be empty")
public String username;
	@NotBlank(message="password cannot be empty")
public String password;
}
