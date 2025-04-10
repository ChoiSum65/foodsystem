package com.coding.demo.domain;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class User {
	
	private Integer id;
	private String name;
	private String password;
	private Integer user_type;
	private Integer role;
	private Integer status;
	private LocalDateTime create_time;
	private LocalDateTime update_time;
	
}
