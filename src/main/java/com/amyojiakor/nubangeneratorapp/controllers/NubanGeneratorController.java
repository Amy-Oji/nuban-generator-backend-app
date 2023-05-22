package com.amyojiakor.nubangeneratorapp.controllers;

import com.amyojiakor.nubangeneratorapp.models.payloads.NubanGeneratorPayload;
import com.amyojiakor.nubangeneratorapp.services.NubanGeneratorService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/nuban")
public class NubanGeneratorController {
    @Autowired
    private final NubanGeneratorService nubanGeneratorService;

    @PostMapping("/generate")
    ResponseEntity<?> gerenateNuban(@RequestBody NubanGeneratorPayload payload){
        return ResponseEntity.ok(nubanGeneratorService.generateNuban(payload));
    }
}