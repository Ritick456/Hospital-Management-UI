package com.capgemini.Hospital_Management_UI.dto;

import lombok.Data;

@Data
public class PatientAppointmentDTO {
	 private String name;
	    private String address;
	    private String phone;
	    private Long insuranceId;
	    private Long ssn;
}