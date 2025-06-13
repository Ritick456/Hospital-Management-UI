package com.capgemini.Hospital_Management_UI.client;

import com.capgemini.Hospital_Management_UI.dto.PatientDTO;
import com.capgemini.Hospital_Management_UI.dto.Response;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(name = "patient-service", url = "${backend.api.url}/patient")
public interface PatientClient {

    @GetMapping
    Response<List<PatientDTO>> getAllPatients(@RequestParam(defaultValue = "0") int page,
                                              @RequestParam(defaultValue = "5") int size);

    @GetMapping("/{ssn}")
    Response<PatientDTO> getPatientBySsn(@PathVariable("ssn") Integer ssn);

    @PostMapping
    Response<String> addPatient(@RequestBody PatientDTO patientDTO);

    @PutMapping("/{ssn}")
    Response<PatientDTO> updatePatient(@PathVariable("ssn") Integer ssn,
                                       @RequestBody PatientDTO patientDTO);
}