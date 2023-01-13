package com.example.demo.DAO.entity;

import java.time.LocalDate;
import java.time.LocalTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name="appointment")
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class AppointmentEntity {
	@Column(name="id")
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
		Integer id;
	@Column(name="date")
	@JsonFormat(pattern = "dd/MM/yyyy")
	LocalDate date;
	
	
	@Column(name="time")
	@JsonFormat(pattern = "HH:mm")
	
	LocalTime time;
	
	@Column(name="status")
	@Min(message="status could be 1 or 0 only", value = 0)
	@Max(message="status could be 1 or 0 only",value=1)
	@Builder.Default
	Integer status=1;
	
	@ManyToOne
	@JoinColumn(name="doctor_id")
	
	DoctorEntity doctorObj;
	
	
	
	@ManyToOne
	@JoinColumn(name="patient_id")
	
	PatientEntity patientObj;
	
	
	
}
