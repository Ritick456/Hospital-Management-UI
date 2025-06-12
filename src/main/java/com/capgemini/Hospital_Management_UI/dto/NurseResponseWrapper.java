package com.capgemini.Hospital_Management_UI.dto;

import java.util.List;

import lombok.Data;

@Data
public class NurseResponseWrapper {
    private int status;
    private String message;
    private List<Nurse> data;  
    private String time;
}
