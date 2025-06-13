package com.capgemini.Hospital_Management_UI;

import com.capgemini.Hospital_Management_UI.dto.AppointmentDTO;
import com.capgemini.Hospital_Management_UI.dto.PageResponse;
import com.capgemini.Hospital_Management_UI.dto.Response;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "appointment", url = "http://localhost:8091/api/appointment")
public interface UserClient {

    @GetMapping
    ResponseEntity<Response<PageResponse<AppointmentDTO>>> fetchAllAppointments(
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "5") int size,
            @RequestParam(value = "sort", defaultValue = "appointmentId") String sort
    );

    @GetMapping("/{id}")
    ResponseEntity<Response<AppointmentDTO>> fetchAppointmentById(
            @PathVariable("id") Long appointmentId
    );

    @PostMapping
    ResponseEntity<Response<AppointmentDTO>> createAppointment(
            @RequestBody AppointmentDTO appointmentDTO
    );

    @PutMapping("/{id}")
    ResponseEntity<Response<AppointmentDTO>> updateAppointment(
            @PathVariable("id") Long appointmentId,
            @RequestBody AppointmentDTO appointmentDTO
    );
}