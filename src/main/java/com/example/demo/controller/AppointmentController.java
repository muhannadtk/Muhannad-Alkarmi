package com.example.demo.controller;

import java.time.LocalTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;


import com.example.demo.DAO.entity.PatientEntity;
import com.example.demo.Utility.TokenUtility;
import com.example.demo.pojo.Result;
import com.example.demo.service.AppointmentService;

@RestController
public class AppointmentController {
@Autowired
private AppointmentService service;
@Autowired
private TokenUtility tokenUtility;

@GetMapping("/showAvailableTime")
public List<String> showAvailableTime(HttpServletRequest request,HttpServletResponse response){
	Result result=tokenUtility.checkToken(request.getHeader("token"));
	if(result.getStatusDiscription().equalsIgnoreCase("success")) {
		return service.showAvailableTime();
	}
	else {
		System.out.println("you dont have access to this method");
		return null;
	}
	
	
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

