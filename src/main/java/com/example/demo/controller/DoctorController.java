package com.example.demo.controller;

import java.io.IOException;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.apache.catalina.User;
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
import com.example.demo.DAO.entity.UserEntity;
import com.example.demo.pojo.Result;
import com.example.demo.service.AppointmentService;
import com.example.demo.service.DoctorService;
import com.example.demo.service.PatientService;
import com.example.demo.Utility.TokenUtility;


@RestController
@RequestMapping("/doctor")
public class DoctorController {
	@Autowired
	private DoctorService service;

	@Autowired
	private AppointmentService appointmentService;
	
	@Autowired
	private TokenUtility tokenUtility;
	
	@PostMapping("/register")
	public Result registerDr(@RequestBody DoctorEntity doctor,HttpServletRequest request, HttpServletResponse response) {
		Result result=tokenUtility.checkToken(request.getHeader("token"));
		if(result.getStatusDiscription().equalsIgnoreCase("success")) {
			return service.addOrUpdateDoctor(doctor);
		}
		else {
			return result;
		}
		
	}
	
	@PutMapping("/updateDoctor")
	public Result updateDoctor(@Valid @RequestBody DoctorEntity doctor,HttpServletRequest request,HttpServletResponse response) {
		
		Result result=tokenUtility.checkToken(request.getHeader("token"));
		if(result.getStatusDiscription().equalsIgnoreCase("success")) {
			return service.addOrUpdateDoctor(doctor);
		}
		else {
			return result;
		}
		
		
		
		
	}
	@GetMapping("/showAllPatients")
	public List<PatientEntity> showAllPatient(HttpServletRequest request,HttpServletResponse response){
		Result result=tokenUtility.checkToken(request.getHeader("token"));
		if(result.getStatusDiscription().equalsIgnoreCase("success")) {
			
			return service.getAllPatient();
		}
		else {
			System.out.println("you dont have access to this method");
			return null;
		}
		
	}
	@PostMapping("/createAppointment")
	public Result createAppointment(@RequestBody AppointmentEntity appointment,HttpServletRequest request, HttpServletResponse response) {
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
	@GetMapping("/showPatientById")
	public Result showPatientById(@RequestParam Integer patientId,HttpServletRequest request,HttpServletResponse response){
		Result result=tokenUtility.checkToken(request.getHeader("token"));
		if(result.getStatusDiscription().equalsIgnoreCase("success")) {
			return service.showPatientById(patientId);
		}
		else {
			return result;
		}
		
	}
	@GetMapping("/showAvailableTime")
	public List<String> showAvailableTime(HttpServletRequest request,HttpServletResponse response){
		Result result=tokenUtility.checkToken(request.getHeader("token"));
		if(result.getStatusDiscription().equalsIgnoreCase("success")) {
			return appointmentService.showAvailableTime();
		}
		else {
			System.out.println("you dont have access to this method");
			return null;
		}
	}
	@GetMapping("/showBookedTimeLine")
	public List<Map<String,Object>> showBookedTimeLine(HttpServletRequest request,HttpServletResponse response){
		Result result=tokenUtility.checkToken(request.getHeader("token"));
		if(result.getStatusDiscription().equalsIgnoreCase("success")) {
			return appointmentService.showBookedTimeLine();
		}
		else {
			System.out.println("you dont have access to this method");
			return null;
		}
	}
	@PutMapping("/updateAppointmentStatus")
	public Result updateAppointmentStatus(@Valid @RequestParam Integer appointmentId,@RequestParam Integer status,HttpServletRequest request,HttpServletResponse response){
		Result result=tokenUtility.checkToken(request.getHeader("token"));
		if(result.getStatusDiscription().equalsIgnoreCase("success")) {
			return service.updateAppointmentStatus(appointmentId,status);
		}
		else {
			return result;
		}
		
	}
	@GetMapping("/showDoctorReport")
	public Result showDoctorReport(@RequestParam Integer doctorId ,@RequestParam @DateTimeFormat(pattern = "dd/MM/yyyy")LocalDate fromDate,@RequestParam @DateTimeFormat(pattern = "dd/MM/yyyy") LocalDate toDate,HttpServletRequest request,HttpServletResponse response) throws IOException{
		Result result=tokenUtility.checkToken(request.getHeader("token"));
		if(result.getStatusDiscription().equalsIgnoreCase("success")) {
			return service.showDoctorReport(doctorId,fromDate,toDate);
		}
		else {
			return result;
		}
		
	}
	@GetMapping("/countOfPatientVisits")
	public Result countOfPatientVisits(@RequestParam Integer doctorId ,@RequestParam Integer patientId,HttpServletRequest request,HttpServletResponse response) {
		Result result=tokenUtility.checkToken(request.getHeader("token"));
		if(result.getStatusDiscription().equalsIgnoreCase("success")) {
			return service.countOfPatientVisits(doctorId,patientId);
		}
		else {
			return result;
		}
	}
	
	
	
	
	@GetMapping("/generateToken")
	public Result generateToken() {
		
		
		String token=tokenUtility.generateToken("test");
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
