package com.example.demo.service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.DAO.entity.AppointmentEntity;
import com.example.demo.DAO.repository.AppointmentRepo;
import com.example.demo.DAO.repository.DoctorRepo;
import com.example.demo.DAO.repository.PatientRepo;
import com.example.demo.pojo.Result;

@Service
public class AppointmentService {
	@Autowired
	private AppointmentRepo repo;
	@Autowired
	private DoctorRepo doctorRepo;
	@Autowired
	private PatientRepo patientRepo;
	
	
	
	
	public Result createOrUpdateAppointment(AppointmentEntity appointment) {
		Result result=new Result();
		Map<String,Object> mapResult=new HashMap<>();
		
		if(appointment.getId() !=null) {
			if(appointment.getId()<0) {
			result.setStatusCode(":1");
			result.setStatusDiscription("failed");
			mapResult.put("Id", "Cannot Send Id negative");
			result.setResult(mapResult);
			return result;}
		}
		if(appointment.getDate().isBefore(LocalDate.now())) {
			result.setStatusCode(":1");
			result.setStatusDiscription("failed");
			mapResult.put("Date", "Cannot create appointment in the past");
			result.setResult(mapResult);
			return result;
		}
		if(appointment.getTime().isBefore(LocalTime.of(8, 0))||appointment.getTime().isAfter(LocalTime.of(17, 0))) {
			result.setStatusCode(":1");
			result.setStatusDiscription("failed");
			mapResult.put("Time", "Cannot create appointment out of work time");
			result.setResult(mapResult);
			return result;
		}
		if(doctorRepo.findById(appointment.getDoctorObj().getId()).isEmpty()) {
			result.setStatusCode(":1");
			result.setStatusDiscription("failed");
			mapResult.put("Doctor Id", "there is no doctor with this Id");
			result.setResult(mapResult);
			return result;
			
		}
		if(patientRepo.findById(appointment.getPatientObj().getId()).isEmpty()) {
			result.setStatusCode(":1");
			result.setStatusDiscription("failed");
			mapResult.put("Patient Id", "there is no patient with this Id");
			result.setResult(mapResult);
			return result;
			
		}
		AppointmentEntity existingAppointment =repo.findByDoctorIdAndDateAndTime(appointment.getDoctorObj().getId(), appointment.getDate(), appointment.getTime());
		if (existingAppointment != null) {
			result.setStatusCode(":1");
			result.setStatusDiscription("failed");
			mapResult.put("Doctor busy", "You cannot create appointment in this time");
			result.setResult(mapResult);
			return result;
		}
		existingAppointment=repo.findByPatientIdAndDateAndTime(appointment.getPatientObj().getId(), appointment.getDate(), appointment.getTime());
		if (existingAppointment != null) {
			result.setStatusCode(":1");
			result.setStatusDiscription("failed");
			mapResult.put("Patient", "Patient has already appointment this and time");
			result.setResult(mapResult);
			return result;
		}
		
		
		
		repo.save(appointment);
		result.setStatusCode(":0");
		result.setStatusDiscription("Success");
		mapResult.put("Creation", "appointment created successfully");
		result.setResult(mapResult);
		return result;
	}
	public Result CancelAppointment(Integer appointmentId) {
		Result result=new Result();
		Map<String,Object> mapResult=new HashMap<>();
		
		if(appointmentId==null||appointmentId<0) {
			result.setStatusCode(":1");
			result.setStatusDiscription("failed");
			mapResult.put("Id", "Cannot Send Id negative");
			result.setResult(mapResult);
			return result;
			
		}
		Optional<AppointmentEntity> appointment= repo.findById(appointmentId);
		if(!appointment.isPresent()) {
			result.setStatusCode(":1");
			result.setStatusDiscription("failed");
			mapResult.put("appointment Id", "there is no appointment with this Id");
			result.setResult(mapResult);
		return result;
		}
		
		repo.deleteById(appointmentId);
		result.setStatusCode(":0");
		result.setStatusDiscription("Success");
		mapResult.put("Cancle", "appointment Cancled successfully");
		result.setResult(mapResult);
		return result;
			
	}
	public List<String> showAvailableTime() {
		LocalDate currentDate=LocalDate.now();
		
		List<AppointmentEntity> appointments=repo.findByDate(currentDate);
		List<String> busyTime=new ArrayList<>();
		List<String> allTime=new ArrayList<>();
		
		LocalTime time=LocalTime.of(8,0);
		while(time.isBefore(LocalTime.of(17, 0))) {
		allTime.add("Time: "+time.format(DateTimeFormatter.ofPattern("HH:mm")));
		time=time.plusMinutes(60);}
		
		if(!appointments.isEmpty()) {
		for(AppointmentEntity appointment :appointments) {
			LocalTime start=appointment.getTime();
			LocalTime end=appointment.getTime().plusMinutes(60);
			while(start.isBefore(end)) {
				busyTime.add(start.format(DateTimeFormatter.ofPattern("HH:mm")));
				start=start.plusMinutes(60);
			}

		}
	    
	        allTime.removeAll(busyTime);
		}
		return allTime;
	}
	
	public List<Map<String,Object>> showBookedTimeLine(){
		LocalDate currentDate=LocalDate.now();
		List<AppointmentEntity> appointments=repo.findByDate(currentDate);
		List<Map<String,Object>> appointmentInfos=new ArrayList<>();
        if(appointments.isEmpty())
            return Collections.singletonList(Collections.singletonMap("Message","There is no appointments today!"));
        else {
		for(AppointmentEntity appointment :appointments) {
			Map<String,Object> appointmentInfo=new HashMap<>();
			appointmentInfo.put("Time", appointment.getTime().format(DateTimeFormatter.ofPattern("HH:mm")));
			appointmentInfo.put("patient Id", appointment.getPatientObj().getId());
			appointmentInfo.put("patient Name", appointment.getPatientObj().getName());
			appointmentInfos.add(appointmentInfo);
		}

       
        }
		return appointmentInfos;
	}
	

}
