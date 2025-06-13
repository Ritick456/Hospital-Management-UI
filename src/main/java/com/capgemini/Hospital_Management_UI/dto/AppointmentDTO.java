package com.capgemini.Hospital_Management_UI.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class AppointmentDTO {
    private Long appointmentId;
    private LocalDateTime start;
    private LocalDateTime end;
    private String examinationRoom;
    private PhysicianAppointmentDTO physician;
    private NurseAppointmentDTO nurse;
    private PatientAppointmentDTO patient;
}