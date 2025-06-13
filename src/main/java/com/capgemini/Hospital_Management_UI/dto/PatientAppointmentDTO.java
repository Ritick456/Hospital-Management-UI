package com.capgemini.Hospital_Management_UI.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PatientAppointmentDTO {
	 private String name;
	    private String address;
	    private String phone;
	    private Long insuranceId;
	    private Long ssn;
}