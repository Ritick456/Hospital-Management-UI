package com.capgemini.Hospital_Management_UI.client;

import com.capgemini.Hospital_Management_UI.dto.DepartmentDto;
import com.capgemini.Hospital_Management_UI.dto.PageResponse;
import com.capgemini.Hospital_Management_UI.dto.PhysicianDto;
import com.capgemini.Hospital_Management_UI.dto.Response;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(name = "hospital-management-system", url = "http://localhost:8090")
public interface AffiliationFeignClient {

    @GetMapping("/api/affiliated_with")
    ResponseEntity<Response<PageResponse<com.capgemini.Hospital_Management_UI.dto.ResponseAffiliatedDto>>> getAllAffiliatedWith(
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "20") int size,
            @RequestParam(value = "sort", defaultValue = "physician") String sort,
            @RequestParam(value = "physicianName", required = false) String physicianName,
            @RequestParam(value = "departmentName", required = false) String departmentName
    );

    @PostMapping("/api/affiliated_with/post")
    ResponseEntity<Response<String>> createAffiliated(@RequestBody com.capgemini.Hospital_Management_UI.dto.AffiliatedWithDto affiliatedWithDto);

    @GetMapping("/api/physician")
    ResponseEntity<Response<List<PhysicianDto>>> getAllPhysicians();

    @GetMapping("/api/department/")
    ResponseEntity<Response<List<DepartmentDto>>> getAllDepartment();
}
