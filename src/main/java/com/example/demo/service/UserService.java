package com.example.demo.service;

import java.util.HashMap;
import java.util.Map;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.DAO.entity.UserEntity;
import com.example.demo.DAO.repository.UserRepo;
import com.example.demo.Utility.TokenUtility;
import com.example.demo.pojo.Login;
import com.example.demo.pojo.Result;
@Service
public class UserService {
@Autowired
UserRepo repo;
@Autowired TokenUtility  tokenUtility;

public Result login(Login login) {
	Result result=new Result();
	Map<String,Object> mapResult=new HashMap<>();
	UserEntity user=repo.findByUsername(login.getUsername());
	if(user==null||!(user.getUsername().equalsIgnoreCase(login.username))) {
		result.setStatusCode(":1");
		result.setStatusDiscription("failed");
		mapResult.put("username"," User Not Found or Incorrect username");
		result.setResult(mapResult);
		return result;
		}
	if(!(user.getPassword().equalsIgnoreCase(login.getPassword()))) {
		result.setStatusCode(":1");
		result.setStatusDiscription("failed");
		mapResult.put("password"," Incorrect password");
		result.setResult(mapResult);
		return result;
	}
	String token=tokenUtility.generateToken(login.getUsername());
	result.setStatusCode(":0");
	result.setStatusDiscription("success");
	mapResult.put("token",token);
	result.setResult(mapResult);
	return result;
}
}
