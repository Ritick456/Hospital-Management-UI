package com.capgemini.Hospital_Management_UI.controller;

import java.util.Collections;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.capgemini.Hospital_Management_UI.dto.Nurse;
import com.capgemini.Hospital_Management_UI.dto.NurseResponseWrapper;
import com.capgemini.Hospital_Management_UI.dto.PatientAppointmentDTO;
import com.capgemini.Hospital_Management_UI.dto.Response;
import com.capgemini.Hospital_Management_UI.client.NurseClient;

import feign.FeignException;
import lombok.AllArgsConstructor;
@Controller
@AllArgsConstructor
public class NurseController {
	
	private final NurseClient nurseClient;
	
	 @GetMapping("/nurses")
	    public String getAllNurses(
	            @RequestParam(defaultValue = "0") int page,
	            @RequestParam(defaultValue = "2") int size,
	            @RequestParam(required = false) String keyword,
	            Model model) {

	        try {
	            NurseResponseWrapper response = nurseClient.getAllNurse(page, size, keyword);

	            model.addAttribute("nurses", response.getData());
	            model.addAttribute("currentPage", page);
	            model.addAttribute("pageSize", size);
	            model.addAttribute("keyword", keyword);
	            model.addAttribute("isLastPage", response.getData().size() < size); // crude last page check
	        } catch (feign.FeignException.NotFound ex) {
	            model.addAttribute("nurses", Collections.emptyList());
	            model.addAttribute("message", "❌ No nurses found. Please try a different search or check the page number.");
	        }

	        return "nurses";
	    }

	    // ✏️ Show nurse edit form
	    @GetMapping("/nurses/edit/{id}")
	    public String showEditForm(@PathVariable("id") int id, Model model) {
	        try {
	            ResponseEntity<Response<Nurse>> response = nurseClient.getNurseById(id);
	            Nurse nurse = response.getBody().getData();
	            model.addAttribute("nurse", nurse);
	            return "edit-nurse";
	        } catch (FeignException.NotFound ex) {
	            model.addAttribute("errorMessage", "❌ Nurse not found.");
	            return "redirect:/nurses";
	        } catch (FeignException ex) {
	            model.addAttribute("errorMessage", "❗ Error retrieving nurse details.");
	            return "redirect:/nurses";
	        }
	    }

	    // 💾 Update nurse
	    @PostMapping("/nurses/update")
	    public String updateNurse(@ModelAttribute Nurse nurse, Model model) {
	        try {
	            nurseClient.updateNursePartially(nurse.getEmployeeId(), nurse);
	            return "redirect:/nurses";
	        } catch (FeignException.NotFound ex) {
	            model.addAttribute("errorMessage", "❌ Nurse not found for update.");
	            return "edit-nurse";
	        } catch (FeignException ex) {
	            model.addAttribute("errorMessage", "❗ Failed to update nurse.");
	            return "edit-nurse";
	        }
	    }

	    // ➕ Show nurse add form
	    @GetMapping("/nurses/add")
	    public String showAddNurseForm(Model model) {
	        model.addAttribute("nurse", new Nurse());
	        return "add-nurse";
	    }

	    // ➕ Add nurse with duplicate ID handling
	    @PostMapping("/nurses/add")
	    public String addNurse(@ModelAttribute Nurse nurse, Model model) {
	        try {
	            nurseClient.addNurse(nurse);
	            return "redirect:/nurses";
	        } catch (FeignException.Conflict ex) {
	            model.addAttribute("nurse", nurse);
	            model.addAttribute("errorMessage", "🚫 A nurse with this Employee ID already exists. Please try a different one.");
	            return "add-nurse";
	        } catch (FeignException ex) {
	            model.addAttribute("nurse", nurse);
	            model.addAttribute("errorMessage", "❗ An error occurred while adding the nurse.");
	            return "add-nurse";
	        }
	    }

	    @GetMapping("/nurses/{id}/patients")
	    public String viewPatientsByNurse(@PathVariable("id") Integer nurseId, Model model) {
	        try {
	            ResponseEntity<Response<List<PatientAppointmentDTO>>> response = nurseClient.getPatientsByNurseId(nurseId);

	            if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
	                List<PatientAppointmentDTO> patients = response.getBody().getData();
	                if (patients != null && !patients.isEmpty()) {
	                    model.addAttribute("patients", patients);
	                } else {
	                    model.addAttribute("error", "No patients found for nurse ID: " + nurseId);
	                }
	            } else {
	                model.addAttribute("error", "Failed to fetch patients. Please try again later.");
	            }

	        } catch (FeignException.NotFound e) {
	            model.addAttribute("error", "No patients found for nurse ID: " + nurseId);
	        } catch (FeignException e) {
	            model.addAttribute("error", "Error fetching patients: " + e.getMessage());
	        }

	        return "nurse-patients";
	    }
}
