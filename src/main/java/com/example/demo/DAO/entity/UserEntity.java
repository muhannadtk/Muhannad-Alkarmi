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

import com.fasterxml.jackson.annotation.JsonFormat;


import lombok.Data;

@Entity
@Table(name = "user")
@Data
public class UserEntity {
	@Column(name = "id")
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)

	Integer id;
	@Column(name = "username")
	String username;
	@Column(name = "password")
	String password;
	
	
	
}
