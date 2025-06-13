package com.capgemini.Hospital_Management_UI.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class PatientAppointmentDTO {

    private String name;

    private String address;

    private String phone;

    private Integer insuranceId;

    private Integer ssn;
}
