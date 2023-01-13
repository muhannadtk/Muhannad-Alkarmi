package com.example.demo.service;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.DAO.entity.AppointmentEntity;
import com.example.demo.DAO.entity.DoctorEntity;
import com.example.demo.DAO.entity.PatientEntity;
import com.example.demo.DAO.entity.UserEntity;
import com.example.demo.DAO.repository.AppointmentRepo;
import com.example.demo.DAO.repository.DoctorRepo;
import com.example.demo.DAO.repository.PatientRepo;
import com.example.demo.DAO.repository.UserRepo;
import com.example.demo.pojo.Result;
@Service
public class PatientService {
	@Autowired
	private PatientRepo repo;
	@Autowired
	private DoctorRepo doctorRepo;
	@Autowired
	private DoctorService doctorService;
	@Autowired
	private AppointmentRepo appointmentRepo;
	@Autowired 
	UserRepo userRepo;
	
	public Result addOrUpdatePatient(PatientEntity patient) {
		Result result=new Result();
		Map<String,Object> mapResult=new HashMap<>();
		if(patient.getName()==null||patient.getName().isEmpty()) {
			result.setStatusCode(":1");
			result.setStatusDiscription("failed");
			mapResult.put("Name", "Cannot Send Name Empty");
			result.setResult(mapResult);
			return result;
		}
		if(patient.getId() !=null) {
			if(patient.getId()<0) { 
			result.setStatusCode(":1");
			result.setStatusDiscription("failed");
			mapResult.put("Id", "Cannot Send Id negative");
			result.setResult(mapResult);
			return result;}
		}
		if(patient.userObj!=null) {
		UserEntity existingUser =userRepo.findByUsername(patient.userObj.getUsername());
		if (existingUser != null) {
			result.setStatusCode(":1");
			result.setStatusDiscription("failed");
			mapResult.put("username", "Error: A user with the same username already exists.");
			result.setResult(mapResult);
			return result;
		}
		if (patient.userObj.getUsername().isEmpty()) {
			result.setStatusCode(":1");
			result.setStatusDiscription("failed");
			mapResult.put("username", "Error: username cannot be empty or null");
			result.setResult(mapResult);
			return result;
		}
		if (patient.userObj.getPassword().isEmpty()) {
			result.setStatusCode(":1");
			result.setStatusDiscription("failed");
			mapResult.put("password", "Error: password cannot be empty or null");
			result.setResult(mapResult);
			return result;
		
		}
		userRepo.save(patient.userObj);
		}
		repo.save(patient);
		result.setStatusCode(":0");
		result.setStatusDiscription("Success");
		mapResult.put("Regester status", "Patient regestered successfully");
		result.setResult(mapResult);
		return result;
	}
	public List<DoctorEntity> showAllDoctors() {
		return doctorService.getAllDoctors();
		
	}
	public List<DoctorEntity> showAvailableDoctors(LocalDate date,LocalTime time){
		return doctorService.getAvailableDoctors(date, time);
	}
	public List<Map<String,Object>> showPatientReport(Integer patientId,LocalDate fromDate,LocalDate toDate) throws IOException{
		
		
		List<AppointmentEntity> appointments = appointmentRepo.findByPatientIdAndDateBetween(patientId, fromDate, toDate);
		List<Map<String, Object>> times = new ArrayList<>();
		 for (AppointmentEntity appointment : appointments) {
		      Map<String, Object> time = new HashMap<>();
		      time.put("date", appointment.getDate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
		      time.put("Time", appointment.getTime().format(DateTimeFormatter.ofPattern("HH:mm")));
		      time.put("Doctor Id", appointment.getDoctorObj().getId());
		      time.put("Doctor Name", appointment.getDoctorObj().getName());
		      time.put("appointment Id", appointment.getId());
		      times.add(time);
		    }
		

		    return times;
		  }
	
}
