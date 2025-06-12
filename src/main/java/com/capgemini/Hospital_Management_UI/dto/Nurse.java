package com.capgemini.Hospital_Management_UI.dto;

import lombok.Data;

@Data
public class Nurse {
	private int employeeId;
	private String name;
	private String position;
	private boolean registered;
	private Integer ssn;
}
