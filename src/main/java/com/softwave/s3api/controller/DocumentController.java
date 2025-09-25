package com.softwave.s3api.controller;

import com.softwave.s3api.dto.ApiResponse;
import com.softwave.s3api.dto.DocumentInfo;
import com.softwave.s3api.service.S3Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/documents")
@CrossOrigin(origins = "*")
public class DocumentController {

    @Autowired
    private S3Service s3Service;

    @PostMapping("/upload")
    public ResponseEntity<ApiResponse<String>> uploadDocument(
            @RequestParam("file") MultipartFile file,
            @RequestParam(value = "folder", required = false) String folder) {
        
        try {
            if (file.isEmpty()) {
                return ResponseEntity.badRequest()
                        .body(ApiResponse.error("Arquivo não pode estar vazio"));
            }

            String key = s3Service.uploadDocument(file, folder);
            return ResponseEntity.ok(
                    ApiResponse.success("Documento enviado com sucesso", key));
                    
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Erro ao enviar documento: " + e.getMessage()));
        }
    }

    @GetMapping("/list")
    public ResponseEntity<ApiResponse<List<DocumentInfo>>> listDocuments(
            @RequestParam(value = "prefix", required = false) String prefix) {
        
        try {
            List<DocumentInfo> documents = s3Service.listDocuments(prefix);
            return ResponseEntity.ok(
                    ApiResponse.success("Documentos listados com sucesso", documents));
                    
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Erro ao listar documentos: " + e.getMessage()));
        }
    }

    @DeleteMapping("/{key:.+}")
    public ResponseEntity<ApiResponse<Void>> deleteDocument(@PathVariable String key) {
        try {
            if (!s3Service.documentExists(key)) {
                return ResponseEntity.notFound().build();
            }

            s3Service.deleteDocument(key);
            return ResponseEntity.ok(
                    ApiResponse.success("Documento excluído com sucesso"));
                    
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Erro ao excluir documento: " + e.getMessage()));
        }
    }

    @GetMapping("/{key:.+}/download-url")
    public ResponseEntity<ApiResponse<String>> getDownloadUrl(@PathVariable String key) {
        try {
            if (!s3Service.documentExists(key)) {
                return ResponseEntity.notFound().build();
            }

            String downloadUrl = s3Service.generatePresignedUrl(key, java.time.Duration.ofHours(1));
            return ResponseEntity.ok(
                    ApiResponse.success("URL de download gerada com sucesso", downloadUrl));
                    
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Erro ao gerar URL de download: " + e.getMessage()));
        }
    }
}