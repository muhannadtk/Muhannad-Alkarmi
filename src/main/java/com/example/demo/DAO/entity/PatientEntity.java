package com.example.demo.DAO.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
@Entity
@Table(name="patient")
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor

@Data
@Builder
public class PatientEntity {

	@Column(name="id")
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
		Integer id;
	@Column(name="name")
	@NotEmpty(message="Name cannot be empty :")
		String name;
	@Column(name="age")
	@Min(message="Age cannot be les than 0",value=0)
		Integer age;
	@Column(name="phone_number")
		String phoneNumber;
	@Column(name="national_id")
		String nationalId;
	@Column(name="gender")
	@Min(message="gender should be 1 for male 2 for female 3 if its unknown", value = 1)
	@Max(message="gender should be 1 for male 2 for female 3 if its unknown",value=3)
		Integer gender;
	
	@OneToOne
	@JoinColumn(name="id")
	
	public UserEntity userObj;

	
	
}

