package com.example.demo.controller;

import java.text.SimpleDateFormat;
import java.time.LocalDate;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;

import com.example.demo.pojo.Login;
import com.example.demo.pojo.Result;
import com.example.demo.service.UserService;


@RestController
@RequestMapping("/user")
public class UserController {
	
	@Autowired
	private UserService service;
	
	@PostMapping("/login")
	public Result login(@Valid @RequestBody Login login) {
		
		return service.login(login);
	}
	
	
	
	
	
	
	
	
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public Result handleValidationExceptions(MethodArgumentNotValidException ex) {
		Result result =new Result();
		result.setStatusCode(":1");
		result.setStatusDiscription("failed");
		
		Map<String,Object> errors=new HashMap<>();
		ex.getBindingResult().getAllErrors().forEach((error)->{
			String fieldName=((FieldError) error).getField();
			String errorMessage =error.getDefaultMessage();
			errors.put(fieldName, errorMessage);
		});
		result.setResult(errors);
		return result;
		}
	
	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	@ExceptionHandler(Exception.class)
	public Result handleAllExceptionMethod(Exception ex,WebRequest request,HttpServletResponse res) {
		Result result=new Result();
		result.setStatusCode(":1");
		result.setStatusDiscription("failed");
		
		Map<String,Object> errors=new HashMap<>();
		errors.put("exception", ex.getCause());
		result.setResult(errors);
		return result;
		
	}
	
	
	
	
	
	
	

}
