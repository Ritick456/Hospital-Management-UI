package com.capgemini.Hospital_Management_UI.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResponseAffiliatedDto {
    private String physician;
    private String department;
    private Boolean primaryAffiliation;
}

