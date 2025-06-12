package com.capgemini.Hospital_Management_UI.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PhysicianDto {
    private Integer employeeId;
    private String name;
    private String position;
    private Integer ssn;
}
