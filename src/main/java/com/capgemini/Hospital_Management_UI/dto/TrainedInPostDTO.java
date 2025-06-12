package com.capgemini.Hospital_Management_UI.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TrainedInPostDTO {
    private PhysicianDto physician;
    private ProcedureDTO procedure;
    private LocalDateTime certificationDate;
    private LocalDateTime certificationExpires;

}
