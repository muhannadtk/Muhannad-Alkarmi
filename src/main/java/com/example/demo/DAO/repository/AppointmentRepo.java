package com.example.demo.DAO.repository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.RequestBody;

import com.example.demo.DAO.entity.AppointmentEntity;
import com.example.demo.DAO.entity.DoctorEntity;
import com.example.demo.DAO.entity.PatientEntity;

public interface AppointmentRepo extends JpaRepository<AppointmentEntity,Integer> {


	
	List<AppointmentEntity> findByDate(LocalDate currentDate);
	
	
	
	@Query(value = "SELECT COUNT(*) FROM appointment WHERE doctor_id = :doctorId AND patient_id = :patientId", nativeQuery = true)
	int countOfPatientVisits(@Param("doctorId") Integer doctorId, @Param("patientId") Integer patientId);
	
	
	 List<AppointmentEntity> findByDateAndTime(LocalDate date, LocalTime time);
	 
	 @Query(value = "SELECT * FROM appointment WHERE patient_id = :patientId AND date BETWEEN :fromDate AND :toDate", nativeQuery = true)
	 List<AppointmentEntity> findByPatientIdAndDateBetween(@Param("patientId")Integer patientId,
			 											   @Param("fromDate") LocalDate fromDate,
			 											   @Param("toDate") LocalDate toDate);
	 
	 @Query(value = "SELECT * FROM appointment WHERE doctor_id = :doctorId AND date BETWEEN :fromDate AND :toDate", nativeQuery = true)
	 List<AppointmentEntity> findByDoctorIdAndDateBetween(@Param("doctorId")Integer doctorId,
			 											  @Param("fromDate") LocalDate fromDate,
			 											  @Param("toDate")LocalDate toDate);
	 @Query(value = "SELECT * FROM appointment WHERE doctor_id = :doctorId AND date = :appointmentdate AND time=:appointmenttime", nativeQuery = true)
	 AppointmentEntity findByDoctorIdAndDateAndTime(@Param("doctorId")Integer doctorId,
			 											  @Param("appointmentdate") LocalDate appointmentdate,
			 											  @Param("appointmenttime")LocalTime appointmenttime);
	 @Query(value = "SELECT * FROM appointment WHERE patient_id = :patientId AND date = :appointmentdate AND time=:appointmenttime", nativeQuery = true)
	 AppointmentEntity findByPatientIdAndDateAndTime(@Param("patientId")Integer doctorId,
			 											  @Param("appointmentdate") LocalDate appointmentdate,
			 											  @Param("appointmenttime")LocalTime appointmenttime);
	 
	 
		
}
