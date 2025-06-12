package com.capgemini.Hospital_Management_UI.Dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class PhysicianDepartmentDto {


    private Integer employeeId;

    private String name;

    private String position;

    private Integer ssn;


}
