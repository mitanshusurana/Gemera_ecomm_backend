package com.jewelry.backend.controller;

import com.jewelry.backend.dto.StoreListResponse;
import com.jewelry.backend.service.StoreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/stores")
public class StoreController {

    @Autowired
    StoreService storeService;

    @GetMapping
    public ResponseEntity<StoreListResponse> getAllStores() {
        return ResponseEntity.ok(storeService.getAllStores());
    }
}
