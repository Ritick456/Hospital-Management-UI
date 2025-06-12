package com.capgemini.Hospital_Management_UI.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ProcedureDTO {
    private Integer code;
    private String name;
    private Double cost;
}

