package com.capgemini.Hospital_Management_UI.Dto;


import lombok.Data;

@Data
public class AffiliatedWithDto {

    private Integer physicianId;
    private Integer departmentId;
    private Boolean primaryAffiliation;

}
