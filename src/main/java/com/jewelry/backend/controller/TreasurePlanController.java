package com.jewelry.backend.controller;

import com.jewelry.backend.dto.EnrollRequest;
import com.jewelry.backend.dto.TreasurePlanResponse;
import com.jewelry.backend.service.TreasurePlanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.security.Principal;

@RestController
@RequestMapping("/api/v1/treasure")
public class TreasurePlanController {

    @Autowired
    TreasurePlanService treasurePlanService;

    @GetMapping("/account")
    public ResponseEntity<TreasurePlanResponse> getAccount(Principal principal) {
        return ResponseEntity.ok(treasurePlanService.getAccount(principal.getName()));
    }

    @PostMapping("/enroll")
    public ResponseEntity<TreasurePlanResponse> enroll(@RequestBody EnrollRequest request, Principal principal) {
        return ResponseEntity.ok(treasurePlanService.enroll(principal.getName(), request));
    }
}
