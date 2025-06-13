package com.capgemini.Hospital_Management_UI.client;


import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import com.capgemini.Hospital_Management_UI.dto.Nurse;
import com.capgemini.Hospital_Management_UI.dto.NurseResponseWrapper;
import com.capgemini.Hospital_Management_UI.dto.PatientAppointmentDTO;
import com.capgemini.Hospital_Management_UI.dto.Response;

@FeignClient(name = "nurse", url = "http://localhost:8091")
public interface NurseClient {

    @GetMapping("/api/nurse")
    NurseResponseWrapper getAllNurse(
        @RequestParam(name = "pageNumber", defaultValue = "0") int pageNumber,
        @RequestParam(name = "pageSize", defaultValue = "2") int pageSize,
        @RequestParam(name = "keyword", required = false) String keyword 
    );
    
    @PutMapping("/api/nurse/{id}")
    Response<Nurse> updateNursePartially(
        @PathVariable("id") int id,
        @RequestBody Nurse nurse
    );


    @GetMapping("/api/nurse/{id}")
    ResponseEntity<Response<Nurse>> getNurseById(@PathVariable("id") int id);
    
    @PostMapping("/api/nurse")
    Response<Nurse> addNurse(@RequestBody Nurse nurse);
    
    @GetMapping("api/appointment/patient/by-nurse")
    ResponseEntity<Response<List<PatientAppointmentDTO>>> getPatientsByNurseId(@RequestParam("nurseId") Integer nurseId);

}
