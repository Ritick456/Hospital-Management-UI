package com.capgemini.Hospital_Management_UI.controller;


import com.capgemini.Hospital_Management_UI.dto.*;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.client.RestOperations;
import org.springframework.web.client.RestTemplate;


import java.util.Arrays;
import java.util.List;

@Controller
public class DepartmentController{

    @GetMapping("/show-departments")
    public String getAllDepartments(@RequestParam(defaultValue = "0") int page,
                                    @RequestParam(defaultValue = "5") int size,
                                    Model model) {

        RestTemplate restTemplate = new RestTemplate();

        String apiUrl = "http://localhost:8091/api/department/?page=" + page + "&size=" + size;

        ParameterizedTypeReference<Response<List<DepartmentDto>>> responseType =
                new ParameterizedTypeReference<>() {};

        ResponseEntity<Response<List<DepartmentDto>>> response = restTemplate.exchange(
                apiUrl,
                HttpMethod.GET,
                null,
                responseType
        );

        List<DepartmentDto> departments = response.getBody().getData();

        model.addAttribute("departments", departments);
        model.addAttribute("currentPage", page);
        // totalPages = calculate manually or hardcode for now if not returned from backend
        model.addAttribute("totalPages", 2); //UPDATE THIS TO REAL VALUE

        return "department-list";
    }



    @GetMapping("/department/edit/{id}")
    public String showEditForm(@PathVariable Integer id, Model model) {
        RestTemplate restTemplate = new RestTemplate();

        DepartmentDto department = restTemplate.getForObject(
                "http://localhost:8091/api/department/" + id,
                DepartmentDto.class
        );

        if (department.getDepartmentId() == null) {
            department.setDepartmentId(id);
        }

        // Use ParameterizedTypeReference to extract generic List<PhysicianDto>
        ResponseEntity<Response<List<PhysicianDto>>> response = restTemplate.exchange(
                "http://localhost:8091/api/physician",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<Response<List<PhysicianDto>>>() {}
        );

        List<PhysicianDto> physicians = response.getBody().getData();

        model.addAttribute("department", department);
        model.addAttribute("physicians", physicians);

        return "edit-department-form";
    }



    @PostMapping("/department/update/{id}")
    public String updateDepartmentHead(@PathVariable Integer id,
                                       @RequestParam("physicianId") Integer physicianId) {

        RestTemplate restTemplate = new RestTemplate();

        // Create DTO object and set physicianId
        UpdateDepartmentHeadRequest dto = new UpdateDepartmentHeadRequest();
        dto.setPhysicianId(physicianId);

        // Prepare JSON request
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<UpdateDepartmentHeadRequest> requestEntity = new HttpEntity<>(dto, headers);

        //Send PUT request to backend
        String updateUrl = "http://localhost:8091/api/department/update/headid/" + id;

        try {
            restTemplate.exchange(updateUrl, HttpMethod.PUT, requestEntity, Void.class);
        } catch (Exception e) {
            e.printStackTrace(); // Log any error
        }

        return "redirect:/show-departments";
    }
    @GetMapping("/department/search-physician")
    public String searchPhysiciansByDept(@RequestParam("deptId") Integer deptId, Model model) {
        RestTemplate restTemplate = new RestTemplate();
        String apiUrl = "http://localhost:8091/api/affiliated_with/physicians/" + deptId;

        // Use a wrapper to capture the outer structure
        ParameterizedTypeReference<Response<List<PhysicianDto>>> responseType =
                new ParameterizedTypeReference<>() {};

        ResponseEntity<Response<List<PhysicianDto>>> response =
                restTemplate.exchange(apiUrl, HttpMethod.GET, null, responseType);

        List<PhysicianDto> physicians = response.getBody().getData();

        model.addAttribute("physicians", physicians);
        model.addAttribute("selectedDeptId", deptId);

        return "physician-list";
    }


    @GetMapping("/show-add-department-form")
    public String showAddDepartmentForm(Model model) {
        CreateDepartmentDto departmentDto = new CreateDepartmentDto();
        departmentDto.setAffiliatedPhysicians(List.of(new AffiliatedPhysicianDto())); // one empty physician for now
        model.addAttribute("department", departmentDto);
        return "add-department";
    }

    @PostMapping("/create-department")
    public String createDepartment(@ModelAttribute("department") CreateDepartmentDto dto) {
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.postForObject("http://localhost:8091/api/department", dto, String.class);
        return "redirect:/show-departments";
    }


}






