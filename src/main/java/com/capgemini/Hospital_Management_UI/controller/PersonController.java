package com.capgemini.Hospital_Management_UI.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.capgemini.Hospital_Management_UI.dto.Nurse;
import com.capgemini.Hospital_Management_UI.dto.NurseResponseWrapper;
import com.capgemini.Hospital_Management_UI.dto.Response;
import com.capgemini.Hospital_Management_UI.feignService.NurseClient;

import feign.FeignException;
import lombok.AllArgsConstructor;

@Controller
@AllArgsConstructor
public class PersonController {

    private final NurseClient nurseClient;

    // 👥 View general people page
    @GetMapping("/people")
    public String getPeople(Model model) {
        return "people";
    }

    // 📄 View all nurses with pagination and optional keyword search
    @GetMapping("/nurses")
    public String getAllNurses(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "2") int size,
            @RequestParam(required = false) String keyword,
            Model model) {

        NurseResponseWrapper response = nurseClient.getAllNurse(page, size, keyword);

        model.addAttribute("nurses", response.getData());
        model.addAttribute("currentPage", page);
        model.addAttribute("pageSize", size);
        model.addAttribute("keyword", keyword);
        model.addAttribute("isLastPage", response.getData().size() < size); // crude last page check

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
}
