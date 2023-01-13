package com.example.demo.DAO.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.DAO.entity.PatientEntity;

public interface PatientRepo extends JpaRepository<PatientEntity,Integer> {

}
