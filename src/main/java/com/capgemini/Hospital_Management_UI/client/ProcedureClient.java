package com.capgemini.Hospital_Management_UI.client;

import com.capgemini.Hospital_Management_UI.dto.PhysicianDto;
import com.capgemini.Hospital_Management_UI.dto.ProcedureDTO;
import com.capgemini.Hospital_Management_UI.dto.Response;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@FeignClient(name = "procedure", url = "http://localhost:8091/api/procedures")
public interface ProcedureClient {
    @GetMapping
    ResponseEntity<Response<List<ProcedureDTO>>> getAllProcedures();

}
