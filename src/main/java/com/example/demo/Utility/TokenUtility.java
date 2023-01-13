package com.example.demo.Utility;

import java.sql.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.example.demo.DAO.entity.UserEntity;
import com.example.demo.pojo.Result;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;

@Component
public class TokenUtility {
public final Integer expDate=60000;
public final String keySign="Test";

public String generateToken(String username) {
	
	Map<String,Object> test = new HashMap<String,Object>();

	test.put("username", "username");
	
	Date convertedDatetime = new Date(System.currentTimeMillis()+expDate);
	
	return Jwts.builder()
			.setClaims(test)
			//.setExpiration(convertedDatetime)
			.signWith(SignatureAlgorithm.HS512, keySign).compact();
	
	
}
public Result checkToken(String token) {
	Result result=new Result();
	Map<String,Object> resultMap=new HashMap<>();
	
	try {
		Claims s=Jwts.parser().setSigningKey(keySign).parseClaimsJws(token).getBody();
		result.setStatusCode(":0");
		result.setStatusDiscription("success");
		String username = (String) s.get("username");
		String password=(String )s.get("password");
		resultMap.put("username", username);
		resultMap.put("password", password);
		result.setResult(resultMap);
		return result;
	}
	catch(SignatureException ex) {
		result.setStatusCode(":1");
		result.setStatusDiscription("failed");
		
		resultMap.put("error", "Invalid JWT signature");
		result.setResult(resultMap);
		return result;
	}
	catch(MalformedJwtException ex) {
		result.setStatusCode(":1");
		result.setStatusDiscription("failed");
		resultMap.put("error", "Invalid Jwt token");
		result.setResult(resultMap);
		return result;
		
	}
	catch(ExpiredJwtException ex) {
		result.setStatusCode(":1");
		result.setStatusDiscription("failed");
		resultMap.put("error", "Expired Jwt token");
		result.setResult(resultMap);
		return result;
		
	}
	catch(UnsupportedJwtException ex) {
		result.setStatusCode(":1");
		result.setStatusDiscription("failed");
		resultMap.put("error", "Unsupported Jwt token");
		result.setResult(resultMap);
		return result;
		
	}
	catch(IllegalArgumentException  ex) {
		result.setStatusCode(":1");
		result.setStatusDiscription("failed");
		resultMap.put("error", "JWT String is empty ");
		result.setResult(resultMap);
		return result;
		
	}
	catch(Exception e) {
		result.setStatusCode(":1");
		result.setStatusDiscription("failed");
		resultMap.put("error", e.getMessage());
		result.setResult(resultMap);
		return result;
	}
}
}
