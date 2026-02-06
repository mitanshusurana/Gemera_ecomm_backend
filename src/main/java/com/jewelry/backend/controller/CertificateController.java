package com.jewelry.backend.controller;

import com.jewelry.backend.entity.Certificate;
import com.jewelry.backend.service.CertificateService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/certificates")
@CrossOrigin(origins = "*")
@Tag(name = "Certificates", description = "Certificate Verification APIs")
public class CertificateController {

    @Autowired
    private CertificateService certificateService;

    @GetMapping("/{reportNumber}")
    @Operation(summary = "Verify certificate")
    public ResponseEntity<Certificate> verifyCertificate(@PathVariable String reportNumber) {
        return ResponseEntity.ok(certificateService.getCertificate(reportNumber));
    }

    @GetMapping("/{reportNumber}/download")
    @Operation(summary = "Download Certificate PDF")
    public ResponseEntity<byte[]> downloadCertificate(@PathVariable String reportNumber) {
        byte[] pdf = certificateService.downloadCertificate(reportNumber);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=certificate_" + reportNumber + ".pdf")
                .contentType(MediaType.APPLICATION_PDF)
                .body(pdf);
    }
}
