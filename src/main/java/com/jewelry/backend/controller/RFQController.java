package com.jewelry.backend.controller;

import com.jewelry.backend.dto.NegotiationRequestDTO;
import com.jewelry.backend.entity.RFQ;
import com.jewelry.backend.entity.RFQQuote;
import com.jewelry.backend.service.RFQService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.Map;
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

    @GetMapping("/requests/number/{rfqNumber}")
    @Operation(summary = "Get RFQ by Number")
    public ResponseEntity<RFQ> getRequestByNumber(@PathVariable String rfqNumber) {
        return ResponseEntity.ok(rfqService.getRequestByNumber(rfqNumber));
    }

    @GetMapping("/requests/user/{userId}")
    @Operation(summary = "Get User Requests")
    public ResponseEntity<Page<RFQ>> getUserRequests(
            @PathVariable UUID userId,
            @RequestParam(required = false) String status,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(rfqService.getUserRequests(userId, status, PageRequest.of(page, size)));
    }

    @PutMapping("/requests/{id}")
    @Operation(summary = "Update RFQ")
    public ResponseEntity<RFQ> updateRequest(@PathVariable UUID id, @RequestBody Map<String, Object> updates) {
        return ResponseEntity.ok(rfqService.updateRequest(id, updates));
    }

    @PostMapping("/requests/{id}/cancel")
    @Operation(summary = "Cancel RFQ")
    public ResponseEntity<Void> cancelRequest(@PathVariable UUID id) {
        rfqService.cancelRequest(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/requests/{id}/quote")
    @Operation(summary = "Get Latest Quote")
    public ResponseEntity<RFQQuote> getLatestQuote(@PathVariable UUID id) {
        return ResponseEntity.ok(rfqService.getLatestQuote(id));
    }

    @GetMapping("/requests/{id}/quotes")
    @Operation(summary = "Get All Quotes for RFQ")
    public ResponseEntity<Page<RFQQuote>> getAllQuotes(
            @PathVariable UUID id,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(rfqService.getAllQuotes(id, PageRequest.of(page, size)));
    }

    @GetMapping("/requests/{id}/quote/pdf")
    @Operation(summary = "Download Quote PDF")
    public ResponseEntity<byte[]> downloadQuotePdf(@PathVariable UUID id) {
        byte[] pdf = rfqService.generateQuotePdf(id);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=quote.pdf")
                .contentType(MediaType.APPLICATION_PDF)
                .body(pdf);
    }

    @PostMapping("/requests/{id}/accept")
    @Operation(summary = "Accept Quote")
    public ResponseEntity<Void> acceptQuote(@PathVariable UUID id) {
        rfqService.acceptQuote(id);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/requests/{id}/reject")
    @Operation(summary = "Reject Quote")
    public ResponseEntity<Void> rejectQuote(@PathVariable UUID id, @RequestBody Map<String, String> body) {
        rfqService.rejectQuote(id, body.get("reason"));
        return ResponseEntity.ok().build();
    }

    @PostMapping("/requests/{id}/negotiate")
    @Operation(summary = "Request Negotiation")
    public ResponseEntity<Void> negotiate(@PathVariable UUID id, @RequestBody NegotiationRequestDTO request) {
        rfqService.negotiate(id, request);
        return ResponseEntity.ok().build();
    }
}
