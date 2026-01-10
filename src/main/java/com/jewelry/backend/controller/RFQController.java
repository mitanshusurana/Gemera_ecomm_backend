package com.jewelry.backend.controller;

import com.jewelry.backend.entity.RFQ;
import com.jewelry.backend.service.RFQService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/rfq")
public class RFQController {

    @Autowired
    RFQService rfqService;

    @PostMapping("/requests")
    public ResponseEntity<RFQ> createRequest(@RequestBody RFQ rfq, Principal principal) {
        return ResponseEntity.status(201).body(rfqService.createRequest(principal.getName(), rfq));
    }

    @GetMapping("/requests/{id}")
    public ResponseEntity<RFQ> getRequest(@PathVariable UUID id) {
        return ResponseEntity.ok(rfqService.getRequest(id));
    }

    @GetMapping("/requests/user/{userId}")
    public ResponseEntity<List<RFQ>> getUserRequests(Principal principal) {
        return ResponseEntity.ok(rfqService.getUserRequests(principal.getName()));
    }

    @PostMapping("/requests/{rfqId}/cancel")
    public ResponseEntity<RFQ> cancelRequest(@PathVariable UUID rfqId, Principal principal) {
        return ResponseEntity.ok(rfqService.cancelRequest(principal.getName(), rfqId));
    }

    @GetMapping("/statistics")
    public ResponseEntity<Map<String, Object>> getStatistics() {
        return ResponseEntity.ok(rfqService.getStatistics());
    }
}
