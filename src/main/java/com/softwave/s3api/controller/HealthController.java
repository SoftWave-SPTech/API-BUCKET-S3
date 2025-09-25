package com.softwave.s3api.controller;

import com.softwave.s3api.dto.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class HealthController {

    @GetMapping("/health")
    public ResponseEntity<ApiResponse<Map<String, Object>>> health() {
        Map<String, Object> healthData = new HashMap<>();
        healthData.put("status", "UP");
        healthData.put("timestamp", LocalDateTime.now());
        healthData.put("service", "API Bucket S3");
        healthData.put("version", "1.0-SNAPSHOT");
        
        return ResponseEntity.ok(
                ApiResponse.success("Serviço funcionando corretamente", healthData));
    }

    @GetMapping("/")
    public ResponseEntity<ApiResponse<String>> welcome() {
        return ResponseEntity.ok(
                ApiResponse.success("Bem-vindo à API de Documentos Jurídicos", 
                        "API para gerenciamento de documentos jurídicos no bucket AWS S3"));
    }
}