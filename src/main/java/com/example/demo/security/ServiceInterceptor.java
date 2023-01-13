package com.example.demo.security;

import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import com.example.demo.Utility.TokenUtility;
import com.example.demo.pojo.Result;
import com.fasterxml.jackson.databind.ObjectMapper;

@Component
public class ServiceInterceptor implements HandlerInterceptor {
	@Autowired
	private TokenUtility tokenUtility;
	@Autowired
	private ObjectMapper mapper;
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		
		String token=request.getHeader("token");
		Result result=new Result();
		Map<String,Object> resultMap=new HashMap<>();
		if(token==null||token.isEmpty()) {
			result.setStatusCode(":1");
			result.setStatusDiscription("401");
			resultMap.put("token", "token cannot be null or empty, please put token in header");
			result.setResult(resultMap);
			
			String finalResult=mapper.writeValueAsString(result);
			response.setStatus(401);
			response.setContentType("application/json");
			try (PrintWriter writer=response.getWriter()){
				writer.write(finalResult);
			}
			
			return false;
				}
		else {
			Result resultToken=tokenUtility.checkToken(token);
			if(resultToken.getStatusDiscription().equalsIgnoreCase("success"))
			{
				return true;
			}
			else {
				String finalResult=mapper.writeValueAsString(result);
			response.setStatus(401);
			response.setContentType("application/json");
			try (PrintWriter writer=response.getWriter()){
				writer.write(finalResult);
			}
			
			
		return false;
		}
		}
	}

}
