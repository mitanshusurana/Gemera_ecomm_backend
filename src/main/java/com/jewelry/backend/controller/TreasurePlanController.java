package com.jewelry.backend.controller;

import com.jewelry.backend.dto.TreasureEnrollRequest;
import com.jewelry.backend.entity.TreasureChestAccount;
import com.jewelry.backend.service.TreasurePlanService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping("/api/v1/treasure")
@CrossOrigin(origins = "*")
@Tag(name = "Treasure Plan", description = "Treasure Chest Plan APIs")
public class TreasurePlanController {

    @Autowired
    TreasurePlanService treasurePlanService;

    @GetMapping("/account")
    @Operation(summary = "Get plan details")
    public ResponseEntity<TreasureChestAccount> getAccount(Principal principal) {
        return ResponseEntity.ok(treasurePlanService.getAccount(principal.getName()));
    }

    @PostMapping("/enroll")
    @Operation(summary = "Enroll in new plan")
    public ResponseEntity<TreasureChestAccount> enroll(@RequestBody TreasureEnrollRequest request, Principal principal) {
        return ResponseEntity.ok(treasurePlanService.enroll(principal.getName(), request));
    }
}
