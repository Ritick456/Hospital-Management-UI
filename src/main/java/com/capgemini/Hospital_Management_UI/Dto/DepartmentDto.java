package com.capgemini.Hospital_Management_UI.Dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class DepartmentDto {

    private Integer departmentId;
    private String name;
    private PhysicianDepartmentDto physicianDetail;

}