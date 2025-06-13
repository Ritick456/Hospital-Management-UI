package com.capgemini.Hospital_Management_UI.client;

import com.capgemini.Hospital_Management_UI.dto.*;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

    @FeignClient(name = "physician", url = "http://localhost:8091/api/physician")
public interface PhysicianClient {

        @GetMapping
        ResponseEntity<Response<List<PhysicianDto>>> getAllPhysicians();



}
