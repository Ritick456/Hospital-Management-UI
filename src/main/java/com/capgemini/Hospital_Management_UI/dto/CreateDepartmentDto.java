package com.capgemini.Hospital_Management_UI.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class CreateDepartmentDto {

    private Integer deptId;  // Unique Department ID
    private String name;     // Name of the department

    // Head of department (another DTO)
    private PhysicianAppointmentDTO head;

    // List of affiliated physicians (another DTO)
    private List<AffiliatedPhysicianDto> affiliatedPhysicians;

}
