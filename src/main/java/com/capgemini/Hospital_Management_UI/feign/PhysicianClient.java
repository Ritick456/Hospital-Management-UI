package com.capgemini.Hospital_Management_UI.feign;

import com.capgemini.Hospital_Management_UI.dto.PhysicianDto;
import com.capgemini.Hospital_Management_UI.dto.Response;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(name = "physician-backend", url = "http://localhost:8090/api/physician")
public interface PhysicianClient {

    @GetMapping
    ResponseEntity<Response<List<PhysicianDto>>> getAllPhysicians();

    @PostMapping("/post")
    ResponseEntity<Response<PhysicianDto>> addPhysician(@RequestBody PhysicianDto physicianDto);

    @PutMapping("/update/name/{empid}")
    ResponseEntity<Response<PhysicianDto>> updatePhysicianName(
            @PathVariable("empid") Integer empid,
            @RequestParam("newName") String newName);

    @PutMapping("/update/position/{position}/{employeeId}")
    ResponseEntity<Response<PhysicianDto>> updatePhysicianPosition(
            @PathVariable("position") String position,
            @PathVariable("employeeId") Integer employeeId);

    @PutMapping("/update/ssn/{empid}")
    ResponseEntity<Response<PhysicianDto>> updatePhysicianSSN(
            @PathVariable("empid") Integer empid,
            @RequestParam("newSsn") Integer newSsn);
}
