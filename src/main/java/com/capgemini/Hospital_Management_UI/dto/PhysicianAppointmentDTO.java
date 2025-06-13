package com.capgemini.Hospital_Management_UI.dto;

import lombok.Data;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class PhysicianAppointmentDTO {
    private Integer employeeId;

    private String name;

    private String position;

    private Integer ssn;
}
