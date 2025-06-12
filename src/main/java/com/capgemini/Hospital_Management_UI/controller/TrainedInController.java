package com.capgemini.Hospital_Management_UI.controller;

import com.capgemini.Hospital_Management_UI.client.PhysicianClient;
import com.capgemini.Hospital_Management_UI.client.TrainedInClient;
import com.capgemini.Hospital_Management_UI.dto.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/trainedIn")
public class TrainedInController {
    private final TrainedInClient trainedInClient;
    private final PhysicianClient physicianClient;

    private static final DateTimeFormatter INPUT_OUTPUT_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");
    private static final DateTimeFormatter PARSE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");

    @GetMapping("/certifications")
    public String showCertificationsPage(Model model) {
        model.addAttribute("appointments", Collections.emptyList());
        model.addAttribute("pageResponse", null);
        model.addAttribute("startDateStr", "");
        model.addAttribute("endDateStr", "");

        // 🔧 FIX: Load physicians for the dropdown on initial page load
        try {
            List<PhysicianDto> physicians = physicianClient.getAllPhysicians().getBody().getData();
            model.addAttribute("physicians", physicians);
        } catch (Exception e) {
            model.addAttribute("physicians", Collections.emptyList());
            model.addAttribute("error", "Error loading physicians: " + e.getMessage());
        }

        return "certifications";
    }

    @GetMapping("/dates")
    public String getCertificationsByDate(
            @RequestParam("startDate") String startDateStr,
            @RequestParam("endDate") String endDateStr,
            @PageableDefault(page = 0, size = 2) Pageable pageable,
            Model model) {

        // Load physicians first (needed for the dropdown)
        try {
            List<PhysicianDto> physicians = physicianClient.getAllPhysicians().getBody().getData();
            model.addAttribute("physicians", physicians);
        } catch (Exception e) {
            model.addAttribute("physicians", Collections.emptyList());
        }

        try {
            String normalizedStartDate = normalizeDateTimeString(startDateStr);
            String normalizedEndDate = normalizeDateTimeString(endDateStr);
            LocalDateTime startDate = LocalDateTime.parse(normalizedStartDate, PARSE_FORMATTER);
            LocalDateTime endDate = LocalDateTime.parse(normalizedEndDate, PARSE_FORMATTER);
            PageResponse<TrainedInPostDTO> response = trainedInClient.getByDates(pageable, startDate, endDate).getBody().getData();
            model.addAttribute("appointments", response.getContent());
            model.addAttribute("pageResponse", response);
            model.addAttribute("startDateStr", startDateStr);
            model.addAttribute("endDateStr", endDateStr);
            model.addAttribute("filterType", "dates");
        } catch (Exception e) {
            model.addAttribute("error", "Invalid date format or error fetching certifications: " + e.getMessage());
            model.addAttribute("appointments", Collections.emptyList());
            model.addAttribute("pageResponse", null);
            model.addAttribute("startDateStr", startDateStr);
            model.addAttribute("endDateStr", endDateStr);
            model.addAttribute("filterType", "dates");
        }

        return "certifications";
    }

    @GetMapping("/physicians")
    public String getCertificationsByPhysician(
            @RequestParam(value = "physicianId", required = false) Integer physicianId,
            @PageableDefault(page = 0, size = 2) Pageable pageable,
            Model model) {

        // Load physicians first (always needed for the dropdown)
        try {
            List<PhysicianDto> physicians = physicianClient.getAllPhysicians().getBody().getData();
            model.addAttribute("physicians", physicians);
        } catch (Exception e) {
            model.addAttribute("physicians", Collections.emptyList());
            model.addAttribute("error", "Error loading physicians: " + e.getMessage());
            model.addAttribute("filterType", "physicians");
            return "certifications";
        }

        if (physicianId == null) {
            model.addAttribute("error", "Please select a physician");
            model.addAttribute("appointments", Collections.emptyList());
            model.addAttribute("pageResponse", null);
            model.addAttribute("filterType", "physicians");
            return "certifications";
        }

        try {
            PageResponse<ProcedureDTO> response = trainedInClient.fetchAllProceduresByPhysicianId(physicianId, pageable).getBody().getData();
            model.addAttribute("appointments", response.getContent());
            model.addAttribute("pageResponse", response);
            model.addAttribute("selectedPhysicianId", physicianId);
            model.addAttribute("filterType", "physicians");
        } catch (Exception e) {
            model.addAttribute("error", "Error fetching certifications for physician: " + e.getMessage());
            model.addAttribute("appointments", Collections.emptyList());
            model.addAttribute("pageResponse", null);
            model.addAttribute("filterType", "physicians");
        }

        return "certifications";
    }

    private String normalizeDateTimeString(String dateTimeStr) {
        if (dateTimeStr.length() == 16) {
            return dateTimeStr + ":00";
        }
        return dateTimeStr;
    }

    @GetMapping("/certifications/edit/{physicianId}/{procedureId}")
    public String showEditForm(@PathVariable int physicianId, @PathVariable int procedureId, Model model) {
        TrainedInUpdateDTO trainedInDTO = trainedInClient.getCertification(physicianId, procedureId);
        model.addAttribute("trainedIn", trainedInDTO);
        model.addAttribute("physicianId", physicianId);
        model.addAttribute("procedureId", procedureId);
        model.addAttribute("certificationDateStr", trainedInDTO.getCertificationDate() != null
                ? trainedInDTO.getCertificationDate().format(INPUT_OUTPUT_FORMATTER) : "");
        model.addAttribute("certificationExpiresStr", trainedInDTO.getCertificationExpires() != null
                ? trainedInDTO.getCertificationExpires().format(INPUT_OUTPUT_FORMATTER) : "");
        return "editCertification";
    }

    @PostMapping("/update/{physicianId}/{procedureId}")
    public String updateCertification(
            @PathVariable int physicianId,
            @PathVariable int procedureId,
            @RequestParam("cost") Double cost,
            @RequestParam("certificationExpires") LocalDateTime certificationExpires,
            RedirectAttributes redirectAttributes) {
        TrainedInUpdateDTO updateDTO = new TrainedInUpdateDTO();
        ProcedureDTO procedureDTO = new ProcedureDTO();
        procedureDTO.setCost(cost);
        updateDTO.setProcedure(procedureDTO);
        updateDTO.setCertificationExpires(certificationExpires);

        ResponseEntity<Response<Boolean>> response = trainedInClient.updateCertification(physicianId, procedureId, updateDTO);
        if (response.getBody().getData()) {
            redirectAttributes.addFlashAttribute("message", "Certification updated successfully");
        } else {
            redirectAttributes.addFlashAttribute("error", "Failed to update certification");
        }
        return "redirect:/trainedIn/certifications";
    }

    @GetMapping("/add")
    public String showAddForm(Model model) {
        model.addAttribute("trainedIn", new TrainedInPostDTO());
        return "addCertification";
    }

    @PostMapping("/add")
    public String addCertification(
            @ModelAttribute TrainedInPostDTO trainedIn,
            RedirectAttributes redirectAttributes) {
        try {
            ResponseEntity<Response<TrainedInPostDTO>> response = trainedInClient.addTrainedIn(trainedIn);
            if (response.getBody().getStatus() == 200) {
                redirectAttributes.addFlashAttribute("message", "Certification added successfully");
            } else {
                redirectAttributes.addFlashAttribute("error", "Failed to add certification");
            }
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error adding certification: " + e.getMessage());
        }
        return "redirect:/trainedIn/certifications";
    }
}