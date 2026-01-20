package com.jewelry.backend.controller;

import com.jewelry.backend.entity.RFQ;
import com.jewelry.backend.service.RFQService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/rfq")
@CrossOrigin(origins = "*")
@Tag(name = "RFQ", description = "Request for Quote APIs")
public class RFQController {

    @Autowired
    RFQService rfqService;

    @PostMapping("/requests")
    @Operation(summary = "Create RFQ")
    public ResponseEntity<RFQ> createRequest(@RequestBody RFQ rfq, Principal principal) {
        return ResponseEntity.status(201).body(rfqService.createRequest(principal.getName(), rfq));
    }

    @GetMapping("/requests/{id}")
    @Operation(summary = "Get RFQ details")
    public ResponseEntity<RFQ> getRequest(@PathVariable UUID id) {
        return ResponseEntity.ok(rfqService.getRequest(id));
    }
}
