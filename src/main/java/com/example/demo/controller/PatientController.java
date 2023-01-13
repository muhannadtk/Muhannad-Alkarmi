package com.example.demo.controller;


import java.io.IOException;

import java.time.LocalDate;
import java.time.LocalTime;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;

import com.example.demo.DAO.entity.AppointmentEntity;
import com.example.demo.DAO.entity.DoctorEntity;
import com.example.demo.DAO.entity.PatientEntity;

import com.example.demo.Utility.TokenUtility;
import com.example.demo.pojo.Result;
import com.example.demo.service.AppointmentService;
import com.example.demo.service.PatientService;

@RestController


@RequestMapping("/patient")
public class PatientController {


	@Autowired
	private PatientService service;
	@Autowired 
	private AppointmentService appointmentService;
	
	
	@Autowired
	private TokenUtility tokenUtility;
	
	@PostMapping("/register")
	public Result registerPt(@RequestBody PatientEntity patient,HttpServletRequest request, HttpServletResponse response) {
		Result result=tokenUtility.checkToken(request.getHeader("token"));
		if(result.getStatusDiscription().equalsIgnoreCase("success")) {
			return service.addOrUpdatePatient(patient);
		}
		else {
			return result;
		}
	}
	@PutMapping("/updatePatient")
	public Result updatePatient(@RequestBody PatientEntity patient,HttpServletRequest request, HttpServletResponse response) {
		Result result=tokenUtility.checkToken(request.getHeader("token"));
		if(result.getStatusDiscription().equalsIgnoreCase("success")) {
			return service.addOrUpdatePatient(patient);
		}
		else {
			return result;
		}
	}
	
	
	@PostMapping("/createAppointment")
	public Result createAppointment( @RequestBody AppointmentEntity appointment,HttpServletRequest request, HttpServletResponse response) {
		Result result=tokenUtility.checkToken(request.getHeader("token"));
		if(result.getStatusDiscription().equalsIgnoreCase("success")) {
			return appointmentService.createOrUpdateAppointment(appointment);
		}
		else {
			return result;
		}
	}
	@DeleteMapping("/cancelAppointment")
	public Result cancelAppointment(Integer appointmentId,HttpServletRequest request, HttpServletResponse response) {
		Result result=tokenUtility.checkToken(request.getHeader("token"));
		if(result.getStatusDiscription().equalsIgnoreCase("success")) {
			return appointmentService.CancelAppointment(appointmentId);
		}
		else {
			return result;
		}
	}
	
	@GetMapping("/showAllDoctors")
	public List<DoctorEntity> showAllDoctors(HttpServletRequest request,HttpServletResponse response){
		return service.showAllDoctors();
	}
	@GetMapping("/showAvailableDoctors")
	public List<DoctorEntity> showAvailableDoctors(@RequestParam @DateTimeFormat(pattern = "dd/MM/yyyy") LocalDate date,@RequestParam @DateTimeFormat(pattern="HH:mm") LocalTime time){
		return service.showAvailableDoctors(date, time);
	}
	@GetMapping("/showPatientReport")
	public List<Map<String, Object>> showPatientReport(@RequestParam Integer patientId ,@RequestParam @DateTimeFormat(pattern = "dd/MM/yyyy") LocalDate fromDate,@RequestParam @DateTimeFormat(pattern = "dd/MM/yyyy") LocalDate toDate,HttpServletRequest request,HttpServletResponse response) throws IOException{
		Result result=tokenUtility.checkToken(request.getHeader("token"));
		if(result.getStatusDiscription().equalsIgnoreCase("success")) {
			return service.showPatientReport(patientId,fromDate,toDate);
		}
		else {
			System.out.println("you dont have access to this method");
			return null;
		}
		
	}
	
	@GetMapping("/generateToken")
	public Result generateToken() {
		
		
		String token=tokenUtility.generateToken("username");
		Result result=new Result();
		Map<String,Object> mapResult=new HashMap<>();
		result.setStatusCode(":0");
		result.setStatusDiscription("Success");
		mapResult.put("Token", token);
		result.setResult(mapResult);
		return result;
	}
	
	
	@GetMapping("/validateToken")
	public Result validateToken(@RequestParam String token ) {
		Result resultFromToken=tokenUtility.checkToken(token);
		Result result=new Result();
		Map<String,Object> mapResult=new HashMap<>();
		result.setStatusCode(":0");
		result.setStatusDiscription("Success");
		mapResult.put("Token", resultFromToken);
		result.setResult(mapResult);
		return result;
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
		errors.put("exception", ex.getMessage());
		result.setResult(errors);
		return result;
		
	}
		
	
}

	

