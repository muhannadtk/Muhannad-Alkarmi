package com.example.demo.DAO.repository;


import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.DAO.entity.UserEntity;

public interface UserRepo extends JpaRepository<UserEntity,Integer> {

	UserEntity findByUsername(String username);

	

	
}
