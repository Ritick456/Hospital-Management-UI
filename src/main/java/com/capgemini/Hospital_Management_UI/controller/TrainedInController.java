package com.capgemini.Hospital_Management_UI.controller;

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

@Controller
@RequiredArgsConstructor
@RequestMapping("/trainedIn")
public class TrainedInController {
    private final TrainedInClient trainedInClient;

    private static final DateTimeFormatter INPUT_OUTPUT_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");
    private static final DateTimeFormatter PARSE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");

    @GetMapping("/certifications")
    public String showCertificationsPage(Model model) {
        model.addAttribute("appointments", Collections.emptyList());
        model.addAttribute("pageResponse", null);
        model.addAttribute("startDateStr", "");
        model.addAttribute("endDateStr", "");
        return "certifications";
    }

    @GetMapping("/dates")
    public String getByDates(
            @PageableDefault(size = 2) Pageable pageable,
            @RequestParam("startDate") String startDateStr,
            @RequestParam("endDate") String endDateStr,
            @RequestParam(value = "filter1", required = false) String filter1,
            @RequestParam(value = "filter2", required = false) String filter2,
            Model model) {
        startDateStr = normalizeDateTimeString(startDateStr);
        endDateStr = normalizeDateTimeString(endDateStr);

        LocalDateTime startDate = LocalDateTime.parse(startDateStr, PARSE_FORMATTER);
        LocalDateTime endDate = LocalDateTime.parse(endDateStr, PARSE_FORMATTER);

        PageResponse<TrainedInPostDTO> response = trainedInClient.getByDates(pageable, startDate, endDate)
                .getBody().getData();

        model.addAttribute("appointments", response.getContent());
        model.addAttribute("pageResponse", response);
        model.addAttribute("startDateStr", startDate.format(INPUT_OUTPUT_FORMATTER));
        model.addAttribute("endDateStr", endDate.format(INPUT_OUTPUT_FORMATTER));
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