package com.softwave.s3api.controller;

import com.softwave.s3api.dto.S3ObjectInfo;
import com.softwave.s3api.dto.UploadResponse;
import com.softwave.s3api.service.S3Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.ResponseInputStream;
import software.amazon.awssdk.services.s3.model.GetObjectResponse;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/s3")
@CrossOrigin(origins = "*")
public class S3Controller {

    @Autowired
    private S3Service s3Service;

    /**
     * Upload de arquivo
     */
    @PostMapping("/upload")
    public ResponseEntity<UploadResponse> uploadFile(
            @RequestParam("file") MultipartFile file,
            @RequestParam(value = "folder", required = false) String folder) {
        
        if (file.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }

        String key = s3Service.uploadFile(file, folder);
        String url = s3Service.generatePresignedUrl(key, 60); // URL válida por 1 hora

        UploadResponse response = new UploadResponse(
                key,
                file.getOriginalFilename(),
                "Arquivo enviado com sucesso",
                url
        );

        return ResponseEntity.ok(response);
    }

    /**
     * Download de arquivo
     */
    @GetMapping("/download/{key:.+}")
    public ResponseEntity<InputStreamResource> downloadFile(@PathVariable String key) {
        
        if (!s3Service.fileExists(key)) {
            return ResponseEntity.notFound().build();
        }

        ResponseInputStream<GetObjectResponse> s3Object = s3Service.downloadFile(key);
        
        String fileName = key.contains("/") ? key.substring(key.lastIndexOf("/") + 1) : key;
        
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileName + "\"");
        headers.add(HttpHeaders.CONTENT_TYPE, s3Object.response().contentType());

        return ResponseEntity.ok()
                .headers(headers)
                .body(new InputStreamResource(s3Object));
    }

    /**
     * Deletar arquivo
     */
    @DeleteMapping("/delete/{key:.+}")
    public ResponseEntity<Map<String, String>> deleteFile(@PathVariable String key) {
        
        if (!s3Service.fileExists(key)) {
            return ResponseEntity.notFound().build();
        }

        s3Service.deleteFile(key);

        Map<String, String> response = new HashMap<>();
        response.put("message", "Arquivo deletado com sucesso");
        response.put("key", key);

        return ResponseEntity.ok(response);
    }

    /**
     * Listar arquivos
     */
    @GetMapping("/list")
    public ResponseEntity<List<S3ObjectInfo>> listFiles(
            @RequestParam(value = "prefix", required = false) String prefix) {
        
        List<S3ObjectInfo> files = s3Service.listFiles(prefix);
        return ResponseEntity.ok(files);
    }

    /**
     * Gerar URL pré-assinada
     */
    @GetMapping("/presigned-url/{key:.+}")
    public ResponseEntity<Map<String, String>> generatePresignedUrl(
            @PathVariable String key,
            @RequestParam(value = "expiration", defaultValue = "60") long expirationMinutes) {
        
        if (!s3Service.fileExists(key)) {
            return ResponseEntity.notFound().build();
        }

        String url = s3Service.generatePresignedUrl(key, expirationMinutes);

        Map<String, String> response = new HashMap<>();
        response.put("url", url);
        response.put("key", key);
        response.put("expirationMinutes", String.valueOf(expirationMinutes));

        return ResponseEntity.ok(response);
    }

    /**
     * Verificar se arquivo existe
     */
    @GetMapping("/exists/{key:.+}")
    public ResponseEntity<Map<String, Object>> checkFileExists(@PathVariable String key) {
        
        boolean exists = s3Service.fileExists(key);

        Map<String, Object> response = new HashMap<>();
        response.put("key", key);
        response.put("exists", exists);

        return ResponseEntity.ok(response);
    }

    /**
     * Health check
     */
    @GetMapping("/health")
    public ResponseEntity<Map<String, String>> healthCheck() {
        Map<String, String> response = new HashMap<>();
        response.put("status", "UP");
        response.put("service", "S3 API");
        
        return ResponseEntity.ok(response);
    }
}