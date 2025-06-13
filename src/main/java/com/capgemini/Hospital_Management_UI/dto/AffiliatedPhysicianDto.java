package com.capgemini.Hospital_Management_UI.dto;

import lombok.Data;

@Data
public class AffiliatedPhysicianDto {
    private Integer employeeId;
    private String name;
    private String position;
    private Integer ssn;
    private Boolean primaryAffiliation;
}
