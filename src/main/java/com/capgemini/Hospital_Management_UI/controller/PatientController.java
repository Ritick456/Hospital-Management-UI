package com.capgemini.Hospital_Management_UI.controller;

import com.capgemini.Hospital_Management_UI.client.PatientClient;
import com.capgemini.Hospital_Management_UI.dto.PatientDTO;
import com.capgemini.Hospital_Management_UI.dto.Response;
import feign.FeignException;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.Collections;
import java.util.List;

@Controller
@RequestMapping("/patients")
@RequiredArgsConstructor
public class PatientController {

    private static final Logger logger = LoggerFactory.getLogger(PatientController.class);

    private final PatientClient patientClient;

    @Value("${backend.api.url}")
    private String apiBaseUrl;

    @GetMapping
    public ModelAndView getAllPatients(@RequestParam(defaultValue = "0") int page,
                                       @RequestParam(defaultValue = "5") int size) {
        ModelAndView mav = new ModelAndView("PatientFrontend");
        logger.info("Fetching patients from API at {}/patient with page {} and size {}", apiBaseUrl, page, size);
        try {
            Response<List<PatientDTO>> response = patientClient.getAllPatients(page, size);
            List<PatientDTO> patients = response.getData() != null ? response.getData() : Collections.emptyList();
            mav.addObject("patients", patients);
            mav.addObject("currentPage", page);
            mav.addObject("pageSize", size);
            // Fallback pagination logic: estimate totalPages based on data size
            int totalPages = 1;
            if (!patients.isEmpty()) {
                totalPages = patients.size() < size ? 1 : (int) Math.ceil((double) 50 / size); // Estimate 50 total patients
            }
            mav.addObject("totalPages", totalPages);
        } catch (FeignException e) {
            logger.error("Failed to fetch patients from {}/patient: {}", apiBaseUrl, e.getMessage());
            mav.addObject("patients", Collections.emptyList());
            mav.addObject("currentPage", page);
            mav.addObject("pageSize", size);
            mav.addObject("totalPages", 1);
            mav.addObject("error", "Unable to fetch patients. Please ensure the server is running and try again.");
        }
        return mav;
    }

    @GetMapping("/insert")
    public ModelAndView insertPatientForm() {
        logger.info("Displaying add patient form, API base URL: {}", apiBaseUrl);
        ModelAndView mav = new ModelAndView("PatientForm");
        PatientDTO patient = new PatientDTO();
        patient.setPcpId(1);
        mav.addObject("patient", patient);
        mav.addObject("isEdit", false);
        return mav;
    }

    @PostMapping("/insert")
    public ModelAndView insertPatient(@ModelAttribute PatientDTO patientDTO) {
        logger.info("Adding new patient via API at {}", apiBaseUrl);
        try {
            patientDTO.setPcpId(patientDTO.getPcpId() != null ? patientDTO.getPcpId() : 1);
            patientClient.addPatient(patientDTO);
            return new ModelAndView("redirect:/patients");
        } catch (FeignException e) {
            logger.error("Failed to add patient: {}", e.getMessage());
            ModelAndView mav = new ModelAndView("PatientForm");
            mav.addObject("patient", patientDTO);
            mav.addObject("isEdit", false);
            mav.addObject("error", "Unable to add patient. Please check the input and try again.");
            return mav;
        }
    }

    @GetMapping("/edit")
    public ModelAndView editPatient(@RequestParam("ssn") Integer ssn) {
        logger.info("Fetching patient with SSN {} from API at {}", ssn, apiBaseUrl);
        ModelAndView mav = new ModelAndView("PatientForm");
        try {
            Response<PatientDTO> response = patientClient.getPatientBySsn(ssn);
            mav.addObject("patient", response.getData());
            mav.addObject("isEdit", true);
        } catch (FeignException e) {
            logger.warn("Failed to fetch patient by SSN: {}. Falling back to list fetch.", e.getMessage());
            try {
                Response<List<PatientDTO>> response = patientClient.getAllPatients(0, 1000);
                PatientDTO patient = response.getData().stream()
                        .filter(p -> p.getSsn().equals(ssn))
                        .findFirst()
                        .orElseThrow(() -> new RuntimeException("Patient not found with SSN: " + ssn));
                mav.addObject("patient", patient);
                mav.addObject("isEdit", true);
            } catch (FeignException ex) {
                logger.error("Failed to fetch patient list: {}", ex.getMessage());
                mav.setViewName("PatientFrontend");
                mav.addObject("error", "Unable to fetch patient. Please try again later.");
                mav.addObject("patients", Collections.emptyList());
                mav.addObject("currentPage", 0);
                mav.addObject("pageSize", 5);
                mav.addObject("totalPages", 1);}}
        return mav;
    }

    @PutMapping("/update")
    public ModelAndView updatePatient(@ModelAttribute PatientDTO patientDTO) {
        logger.info("Updating patient with SSN {} via API at {}", patientDTO.getSsn(), apiBaseUrl);
        try {
            patientDTO.setPcpId(patientDTO.getPcpId() != null ? patientDTO.getPcpId() : 1);
            patientClient.updatePatient(patientDTO.getSsn(), patientDTO);
            return new ModelAndView("redirect:/patients");
        } catch (FeignException e) {
            logger.error("Failed to update patient: {}", e.getMessage());
            ModelAndView mav = new ModelAndView("PatientForm");
            mav.addObject("patient", patientDTO);
            mav.addObject("isEdit", true);
            mav.addObject("error", "Failed to update patient. Please try again.");
            return mav;
        }
    }
}