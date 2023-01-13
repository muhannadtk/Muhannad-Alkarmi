package com.example.demo.pojo;

import java.util.Map;

import lombok.Data;

@Data
public class Result {
private String statusCode;

private String statusDiscription;

private Map<String,Object> result;
}
