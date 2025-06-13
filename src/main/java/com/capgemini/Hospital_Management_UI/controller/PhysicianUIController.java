package com.capgemini.Hospital_Management_UI.controller;

import com.capgemini.Hospital_Management_UI.dto.PhysicianDto;
import com.capgemini.Hospital_Management_UI.dto.Response;
import com.capgemini.Hospital_Management_UI.feign.PhysicianClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Map;

@Controller
public class PhysicianUIController {

    @Autowired
    private PhysicianClient physicianClient;

    @GetMapping("/physicians")
    public String showPhysiciansPage(Model model) {
        ResponseEntity<Response<List<PhysicianDto>>> response = physicianClient.getAllPhysicians();

        if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
            model.addAttribute("physicians", response.getBody().getData());
        } else {
            model.addAttribute("physicians", List.of());
            model.addAttribute("error", "Failed to fetch physician data");
        }

        model.addAttribute("newPhysician", new PhysicianDto());

        return "physicians"; // renders templates/physicians.html
    }

    @PostMapping("/physicians/add")
    public String addPhysician(@ModelAttribute PhysicianDto physicianDto, RedirectAttributes redirectAttributes) {
        try {
            ResponseEntity<Response<PhysicianDto>> response = physicianClient.addPhysician(physicianDto);

            if (response.getStatusCode().is2xxSuccessful()) {
                redirectAttributes.addFlashAttribute("message", "Physician added successfully!");
            } else {
                assert response.getBody() != null;
                redirectAttributes.addFlashAttribute("error", "Failed to add physician: " + response.getBody().getMessage());
            }
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Exception while adding physician: " + e.getMessage());
        }

        return "redirect:/physicians";
    }

    @PostMapping("/physicians/update")
    public String updatePhysicianDetails(
            @RequestParam("selectedEmpId") Integer employeeId,
            @RequestParam Map<String, String> params,
            RedirectAttributes redirectAttributes
    ) {
        try {
            // Extract values based on selected ID
            String name = params.get("names[" + employeeId + "]");
            String position = params.get("positions[" + employeeId + "]");
            Integer ssn = Integer.parseInt(params.get("ssns[" + employeeId + "]"));

            // Get existing record
            ResponseEntity<Response<List<PhysicianDto>>> allResponse = physicianClient.getAllPhysicians();
            PhysicianDto existing = allResponse.getBody().getData().stream()
                    .filter(p -> p.getEmployeeId().equals(employeeId))
                    .findFirst()
                    .orElse(null);

            if (existing == null) {
                redirectAttributes.addFlashAttribute("error", "Physician not found.");
                return "redirect:/physicians";
            }

            if (!existing.getName().equals(name)) {
                physicianClient.updatePhysicianName(employeeId, name);
            }

            if (!existing.getPosition().equals(position)) {
                physicianClient.updatePhysicianPosition(position, employeeId);
            }

            if (!existing.getSsn().equals(ssn)) {
                physicianClient.updatePhysicianSSN(employeeId, ssn);
            }

            redirectAttributes.addFlashAttribute("message", "Physician updated successfully!");

        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error updating physician: " + e.getMessage());
        }

        return "redirect:/physicians";
    }



}
