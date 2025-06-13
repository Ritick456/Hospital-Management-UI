package com.capgemini.Hospital_Management_UI.controller;

import com.capgemini.Hospital_Management_UI.client.AffiliationFeignClient;
import com.capgemini.Hospital_Management_UI.dto.DepartmentDto;
import com.capgemini.Hospital_Management_UI.dto.PageResponse;
import com.capgemini.Hospital_Management_UI.dto.PhysicianDto;
import com.capgemini.Hospital_Management_UI.dto.Response;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;

import java.util.Collections;
import java.util.List;

@Controller
@RequestMapping("/aman")
public class AffiliationController {

    private final AffiliationFeignClient affiliationFeignClient;

    public AffiliationController(AffiliationFeignClient affiliationFeignClient) {
        this.affiliationFeignClient = affiliationFeignClient;
    }

    @GetMapping("/")
    public String getAllAffiliations(
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "20") int size,
            @RequestParam(value = "sort", defaultValue = "physician") String sort,
            @RequestParam(value = "physicianName", required = false) String physicianName,
            @RequestParam(value = "departmentName", required = false) String departmentName,
            Model model) {
        try {
            ResponseEntity<Response<PageResponse<com.capgemini.Hospital_Management_UI.Dto.ResponseAffiliatedDto>>> response =
                    affiliationFeignClient.getAllAffiliatedWith(page, size, sort, physicianName, departmentName);
            if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
                PageResponse<com.capgemini.Hospital_Management_UI.Dto.ResponseAffiliatedDto> pageResponse = response.getBody().getData();
                model.addAttribute("affiliations", pageResponse.getContent());
                model.addAttribute("currentPage", pageResponse.getPageNumber());
                model.addAttribute("pageSize", pageResponse.getPageSize());
                model.addAttribute("totalPages", pageResponse.getTotalPages());
                model.addAttribute("totalElements", pageResponse.getTotalElements());
                model.addAttribute("isFirst", pageResponse.isFirst());
                model.addAttribute("isLast", pageResponse.isLast());
                model.addAttribute("physicianName", physicianName);
                model.addAttribute("departmentName", departmentName);
                model.addAttribute("sort", sort);
            } else {
                model.addAttribute("affiliations", Collections.emptyList());
                model.addAttribute("error", "Failed to fetch affiliations");
            }
        } catch (Exception e) {
            model.addAttribute("affiliations", Collections.emptyList());
            model.addAttribute("error", "Error connecting to backend: " + e.getMessage());
        }
        return "affiliated";
    }

    @GetMapping("/add-affiliation")
    public String showAddAffiliationForm(Model model) {
        model.addAttribute("affiliatedWithDto", new com.capgemini.Hospital_Management_UI.Dto.AffiliatedWithDto());
        try {
            System.out.println("Adding affiliation");
            ResponseEntity<Response<List<PhysicianDto>>> physicianResponse = affiliationFeignClient.getAllPhysicians();
            ResponseEntity<Response<List<DepartmentDto>>> departmentResponse = affiliationFeignClient.getAllDepartment();
            System.out.println(physicianResponse.getBody().getData());
            System.out.println(affiliationFeignClient.getAllDepartment().getBody().getData());
            model.addAttribute("physicians", physicianResponse.getStatusCode().is2xxSuccessful() && physicianResponse.getBody() != null
                    ? physicianResponse.getBody().getData() : Collections.emptyList());
            model.addAttribute("departments", departmentResponse.getStatusCode().is2xxSuccessful() && departmentResponse.getBody() != null
                    ? departmentResponse.getBody().getData() : Collections.emptyList());
        } catch (Exception e) {
            model.addAttribute("physicians", Collections.emptyList());
            model.addAttribute("departments", Collections.emptyList());
            model.addAttribute("error", "Error fetching data: " + e.getMessage());
        }
        return "add-affiliation";
    }

    @PostMapping("/add-affiliation")
    public String createAffiliation(@ModelAttribute com.capgemini.Hospital_Management_UI.Dto.AffiliatedWithDto affiliatedWithDto, Model model) {
        try {
            ResponseEntity<Response<String>> response = affiliationFeignClient.createAffiliated(affiliatedWithDto);
            System.out.println(response.getBody().getData());
            if (response.getBody().getMessage().equals("Affiliation created successfully")) {
                return "redirect:/aman/";
            } else {
                model.addAttribute("error", "Failed to create affiliation");
                return showAddAffiliationForm(model);
            }
        } catch (Exception e) {
            model.addAttribute("error", "Error creating affiliation: " + e.getMessage());
            return showAddAffiliationForm(model);
        }

    }
}