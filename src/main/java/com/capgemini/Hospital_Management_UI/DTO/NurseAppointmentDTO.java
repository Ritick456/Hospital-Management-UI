package com.capgemini.Hospital_Management_UI.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class NurseAppointmentDTO {

    private Integer employeeId;

    private String name;

    private String position;

    private Boolean registered;

    private Integer ssn;
}