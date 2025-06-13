package com.capgemini.Hospital_Management_UI.controller;

import com.capgemini.Hospital_Management_UI.dto.AppointmentDTO;
import com.capgemini.Hospital_Management_UI.dto.PageResponse;
import com.capgemini.Hospital_Management_UI.UserClient;
import feign.FeignException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/appointments")
@AllArgsConstructor
public class AppointmentController {

    private final UserClient userClient;

    // 📋 View all appointments
    @GetMapping
    public String getAllAppointments(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size,
            @RequestParam(defaultValue = "appointmentId") String sort,
            Model model) {
        try {
            PageResponse<AppointmentDTO> response = userClient.fetchAllAppointments(page, size, sort).getBody().getData();
            model.addAttribute("appointments", response.getContent());
            model.addAttribute("currentPage", page);
            model.addAttribute("pageSize", size);
            model.addAttribute("sort", sort);
            model.addAttribute("isLastPage", response.getContent().size() < size);
        } catch (FeignException.NotFound ex) {
            model.addAttribute("appointments", java.util.Collections.emptyList());
            model.addAttribute("message", "❌ No appointments found. Please try a different page.");
        } catch (FeignException ex) {
            model.addAttribute("appointments", java.util.Collections.emptyList());
            model.addAttribute("message", "❗ Error retrieving appointments.");
        }
        return "appointments";
    }

    // ➕ Show appointment add form
    @GetMapping("/add")
    public String showAddAppointmentForm(Model model) {
        model.addAttribute("appointment", new AppointmentDTO());
        return "add-appointment";
    }

    // ➕ Add appointment
    @PostMapping("/add")
    public String addAppointment(@ModelAttribute AppointmentDTO appointmentDTO, Model model) {
        try {
            userClient.createAppointment(appointmentDTO);
            return "redirect:/appointments";
        } catch (FeignException ex) {
            model.addAttribute("appointment", appointmentDTO);
            model.addAttribute("errorMessage", "❗ An error occurred while adding the appointment.");
            return "add-appointment";
        }
    }

    // ✏️ Show appointment edit form
    @GetMapping("/edit")
    public String showEditForm(@RequestParam("id") Long id, Model model) {
        try {
            AppointmentDTO appointment = userClient.fetchAppointmentById(id).getBody().getData();
            model.addAttribute("appointment", appointment);
            return "edit-appointment";
        } catch (FeignException.NotFound ex) {
            model.addAttribute("errorMessage", "❌ Appointment not found.");
            return "redirect:/appointments";
        } catch (FeignException ex) {
            model.addAttribute("errorMessage", "❗ Error retrieving appointment details.");
            return "redirect:/appointments";
        }
    }

    // 💾 Update appointment
    @PostMapping("/edit")
    public String updateAppointment(@ModelAttribute AppointmentDTO appointmentDTO, Model model) {
        try {
            userClient.updateAppointment(appointmentDTO.getAppointmentId(), appointmentDTO);
            return "redirect:/appointments";
        } catch (FeignException.NotFound ex) {
            model.addAttribute("errorMessage", "❌ Appointment not found for update.");
            return "edit-appointment";
        } catch (FeignException ex) {
            model.addAttribute("errorMessage", "❗ Failed to update appointment.");
            return "edit-appointment";
        }
    }
}