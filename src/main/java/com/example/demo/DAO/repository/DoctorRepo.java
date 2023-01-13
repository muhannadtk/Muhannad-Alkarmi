package com.example.demo.DAO.repository;



import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.DAO.entity.DoctorEntity;

public interface DoctorRepo extends JpaRepository<DoctorEntity,Integer>{

	
}
