package com.example.demo.service;

import java.io.IOException;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
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
public class DoctorService {
	@Autowired
	DoctorRepo repo;
	@Autowired
	PatientRepo patientRepo;
	@Autowired
	AppointmentRepo appointmentRepo;

	
	@Autowired
	UserRepo userRepo;
	public Result addOrUpdateDoctor(DoctorEntity doctor) {
		Result result=new Result();
		Map<String,Object> mapResult=new HashMap<>();
		if(doctor.getName()==null||doctor.getName().isEmpty()) {
			result.setStatusCode(":1");
			result.setStatusDiscription("failed");
			mapResult.put("Name", "Cannot Send Name Empty");
			result.setResult(mapResult);
			return result;
		}
		if(doctor.getId() !=null) {
			if(doctor.getId()<0) {
			result.setStatusCode(":1");
			result.setStatusDiscription("failed");
			mapResult.put("Id", "Cannot Send Id negative");
			result.setResult(mapResult);
			return result;}
		}
		if(doctor.userObj!=null) {
		UserEntity existingUser =userRepo.findByUsername(doctor.userObj.getUsername());
		if (existingUser != null) {
			result.setStatusCode(":1");
			result.setStatusDiscription("failed");
			mapResult.put("username", "Error: A user with the same username already exists.");
			result.setResult(mapResult);
			return result;
		}
		if (doctor.userObj.getUsername().isEmpty()) {
			result.setStatusCode(":1");
			result.setStatusDiscription("failed");
			mapResult.put("username", "Error: username cannot be empty or null");
			result.setResult(mapResult);
			return result;
		}
		if (doctor.userObj.getPassword().isEmpty()) {
			result.setStatusCode(":1");
			result.setStatusDiscription("failed");
			mapResult.put("password", "Error: password cannot be empty or null");
			result.setResult(mapResult);
			return result;
		}
		userRepo.save(doctor.getUserObj());
		}
		
		repo.save(doctor);
		
		result.setStatusCode(":0");
		result.setStatusDiscription("Success");
		mapResult.put("Regester Status", "Doctor regestered successfully");
		result.setResult(mapResult);
		return result;
	}
	
	public List<PatientEntity> showAllPatients() {
		return patientRepo.findAll();
	}
	public Result updateAppointmentStatus(Integer appointmentId,Integer status) {
		Result result=new Result();
		Map<String,Object> mapResult=new HashMap<>();
		if(appointmentId==null||appointmentId<0) {
			result.setStatusCode(":1");
			result.setStatusDiscription("failed");
			mapResult.put("appointment Id", "Cannot Send appointment Id Empty Or Negative");
			result.setResult(mapResult);
		return result;}
		
		if(status==null||status<0) {
			result.setStatusCode(":1");
			result.setStatusDiscription("failed");
			mapResult.put("status", "Cannot Send status Empty Or Negative it should be 0 or 1");
			result.setResult(mapResult);
		return result;}
		if(status!=0||status!=1) {
			result.setStatusCode(":1");
			result.setStatusDiscription("failed");
			mapResult.put("status", "Cannot Send status should be 0 if patient visited or 1 if patient visited");
			result.setResult(mapResult);
		return result;}
		
		 Optional<AppointmentEntity> optionalAppointment = appointmentRepo.findById(appointmentId);
		 if(optionalAppointment.isEmpty()) {
				result.setStatusCode(":1");
				result.setStatusDiscription("failed");
				mapResult.put("appointment existing", "There is no appointment with this id  ");
				result.setResult(mapResult);
			return result;}
		 
		 AppointmentEntity appointment = optionalAppointment.get();
		 appointment.setStatus(status);
		 appointmentRepo.save(appointment);
		
		result.setStatusCode(":0");
		result.setStatusDiscription("Success");
		mapResult.put("Status", "status updated successfully");
		result.setResult(mapResult);
		return result;
		
	}
	
	public Result showDoctorReport(Integer doctorId ,LocalDate fromDate,LocalDate toDate) throws IOException{
		Result result=new Result();
		Map<String,Object> mapResult=new HashMap<>();
		if(doctorId==null||doctorId<0) {
			result.setStatusCode(":1");
			result.setStatusDiscription("failed");
			mapResult.put("appointment Id", "Cannot Send doctor Id Empty Or Negative");
			result.setResult(mapResult);
		return result;}
		List<AppointmentEntity> appointments = appointmentRepo.findByDoctorIdAndDateBetween(doctorId, fromDate, toDate);
		List<Map<String, Object>> times = new ArrayList<>();
		 for (AppointmentEntity appointment : appointments) {
		      Map<String, Object> time = new HashMap<>();
		      time.put("date", appointment.getDate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
		      time.put("time", appointment.getTime().format(DateTimeFormatter.ofPattern("HH:mm")));
		      time.put("patient Id", appointment.getPatientObj().getId());
		      time.put("patient Name", appointment.getPatientObj().getName());
		      time.put("appointment Id", appointment.getId());
		      times.add(time);
		    }
		 try (Writer writer = Files.newBufferedWriter(Paths.get("DoctorReport.csv"))) {
			  CSVPrinter printer = new CSVPrinter(writer, CSVFormat.DEFAULT.withHeader("Appointment ID", "Patient ID", "Patient Name", "Date", "Time"));
			  for (Map<String, Object> time : times) {
				    printer.printRecord(time.get("appointment Id"), time.get("patient Id"),
				        time.get("patient Name"), time.get("date"), time.get("time"));
				  }

				  printer.flush();
				}

		 	result.setStatusCode(":0");
			result.setStatusDiscription("Success");
			mapResult.put("CSV File", "please check your file location to found your report :)");
			result.setResult(mapResult);
			return result;
		  }
	public Result countOfPatientVisits(Integer doctorId,Integer patientId) {
		Result result=new Result();
		Map<String,Object> mapResult=new HashMap<>();
		if(doctorId==null||doctorId<0) {
			result.setStatusCode(":1");
			result.setStatusDiscription("failed");
			mapResult.put("doctor Id", "Cannot Send doctor Id Empty Or Negative");
			result.setResult(mapResult);
		return result;}
		if(patientId==null||patientId<0) {
			result.setStatusCode(":1");
			result.setStatusDiscription("failed");
			mapResult.put("patient Id", "Cannot Send patient Id Empty Or Negative");
			result.setResult(mapResult);
		return result;}
		if(repo.findById(doctorId).isEmpty()) {
			result.setStatusCode(":1");
			result.setStatusDiscription("failed");
			mapResult.put("doctor Id", "there is no doctor with this Id");
			result.setResult(mapResult);
		return result;}
		if(patientRepo.findById(patientId).isEmpty()) {
			result.setStatusCode(":1");
			result.setStatusDiscription("failed");
			mapResult.put("patient Id", "there is no patient with this Id");
			result.setResult(mapResult);
		return result;}
		
		result.setStatusCode(":0");
		result.setStatusDiscription("Success");
		mapResult.put("Patient visits count", appointmentRepo.countOfPatientVisits(doctorId, patientId));
		result.setResult(mapResult);
		return result;
	}
	public List<DoctorEntity> getAvailableDoctors(LocalDate date,LocalTime time){
		
		   
		    List<AppointmentEntity> bookedAppointments = appointmentRepo.findByDateAndTime(date, time);

		    
		    List<Integer> bookedDoctorIds = bookedAppointments.stream().filter(a->a.getDoctorObj()!=null).map(a -> a.getDoctorObj().getId())
		        .collect(Collectors.toList());

		    
		    List<DoctorEntity> allDoctors = repo.findAll();
		    allDoctors.forEach(d -> d.setUserObj(null));

		    
		    return allDoctors.stream()
		        .filter(d -> !bookedDoctorIds.contains(d.getId()))
		        .collect(Collectors.toList());
		  }
	
		
		

	
	public Result showPatientById(Integer patientId) {
		Result result=new Result();
		Map<String,Object> mapResult=new HashMap<>();
		if(patientId==null||patientId<0) {
			result.setStatusCode(":1");
			result.setStatusDiscription("failed");
			mapResult.put("patient Id", "Cannot Send patient Id Empty Or Negative");
			result.setResult(mapResult);
		return result;}
		
		Optional<PatientEntity> patient=patientRepo.findById(patientId);
		if(!patient.isPresent()) {
			result.setStatusCode(":1");
			result.setStatusDiscription("failed");
			mapResult.put("patient Id", "there is no patient with this Id");
			result.setResult(mapResult);
		return result;
		}
		PatientEntity myPatient=patient.get();
		myPatient.setUserObj(null);
		 	result.setStatusCode(":0");
			result.setStatusDiscription("Success");
			mapResult.put("Patient",myPatient);
			result.setResult(mapResult);
		return result;
	}
	public List<DoctorEntity> getAllDoctors(){
	
	    List<DoctorEntity> allDoctors = repo.findAll();
	    allDoctors.forEach(d -> d.setUserObj(null));
	    return allDoctors;

	    
	  }
	public List<PatientEntity> getAllPatient(){
		
	    List<PatientEntity> allPatient = patientRepo.findAll();
	    allPatient.forEach(d -> d.setUserObj(null));
	    return allPatient;

	    
	  }
	
	
}
