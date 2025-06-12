package com.capgemini.Hospital_Management_UI.client;

import com.capgemini.Hospital_Management_UI.dto.*;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.data.web.PageableDefault;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.domain.Pageable;
@FeignClient(name = "trained-in", url = "http://localhost:8091/api/trained_in")
public interface TrainedInClient {

    @GetMapping("/dates")
    public ResponseEntity<Response<PageResponse<TrainedInPostDTO>>> getByDates(
            @PageableDefault(size = 2) Pageable pageable,
            @RequestParam("startDate")
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam("endDate")
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate
    );

    @PutMapping("/update/{physicianId}/{procedureId}")
    ResponseEntity<Response<Boolean>> updateCertification(
            @PathVariable("physicianId") int physicianId,
            @PathVariable("procedureId") int procedureId,
            @RequestBody TrainedInUpdateDTO updateDTO);

    @GetMapping("/{physicianId}/{procedureId}")
    TrainedInUpdateDTO getCertification(@PathVariable("physicianId") int physicianId, @PathVariable("procedureId") int procedureId);

    @PostMapping
    ResponseEntity<Response<TrainedInPostDTO>> addTrainedIn(@RequestBody TrainedInPostDTO req);

//    @GetMapping("/getAllPhysicians")
//    ResponseEntity<Response<List<PhysicianDto>>> getAllPhysicians();

    @GetMapping("/fetch/physician/{physicianId}")
    ResponseEntity<Response<PageResponse<ProcedureDTO>>> fetchAllProceduresByPhysicianId(
            @PathVariable("physicianId") Integer physicianId,
            Pageable pageable);

    @GetMapping("fetch/procedure/{procedureId}/physicians")
    ResponseEntity<Response<PageResponse<PhysicianAppointmentDTO>>> fetchAllPhysiciansByProcedureId(
            @PathVariable Integer procedureId,
            @PageableDefault(page = 0, size = 5) Pageable pageable
    );
}
