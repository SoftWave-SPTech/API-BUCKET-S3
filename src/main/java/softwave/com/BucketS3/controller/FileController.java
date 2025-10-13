package softwave.com.BucketS3.controller;


import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import softwave.com.BucketS3.dto.UploadResponse;
import softwave.com.BucketS3.service.S3Service;

import java.io.IOException;
import java.util.Map;

@RestController
@RequestMapping("/files")
public class FileController {
    private final S3Service s3Service;

    public FileController(S3Service s3Service) {
        this.s3Service = s3Service;
    }

    @PostMapping("/upload")
    public ResponseEntity<Map<String, String>> upload(
            @RequestParam("file") MultipartFile file,
            @RequestParam(value = "folder", defaultValue = "docs") String folder
    ) throws IOException {
        Map<String, String> response = s3Service.uploadFile(folder, file);
        return ResponseEntity.ok(response);
    }



    @DeleteMapping("/delete")
    public String delete(@RequestParam String key) {
        s3Service.deleteFile(key);
        return "Arquivo deletado: " + key;
    }

    @GetMapping("/download")
    public ResponseEntity<Map<String, String>> downloadFile(
            @RequestParam String key,
            @RequestParam(defaultValue = "5") int minutes) {

        String url = s3Service.generatePresignedUrl(key, minutes);

        // 🔹 Retorna um JSON padrão
        Map<String, String> response = Map.of("url", url);
        return ResponseEntity.ok(response);
    }

}
