package softwave.com.BucketS3.controller;


import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import softwave.com.BucketS3.service.S3Service;

import java.io.IOException;

@RestController
@RequestMapping("/files")
public class FileController {
    private final S3Service s3Service;

    public FileController(S3Service s3Service) {
        this.s3Service = s3Service;
    }

    @PostMapping("/upload")
    public String upload(@RequestParam("file") MultipartFile file,
                         @RequestParam(value = "folder", defaultValue = "docs") String folder) throws IOException {
        return s3Service.uploadFile(folder, file);
    }

    @DeleteMapping("/delete")
    public String delete(@RequestParam String key) {
        s3Service.deleteFile(key);
        return "Arquivo deletado: " + key;
    }

    @GetMapping("/download")
    public ResponseEntity<String> downloadFile(
            @RequestParam String key,
            @RequestParam(defaultValue = "5") int minutes) {
        String url = s3Service.generatePresignedUrl(key, minutes);
        return ResponseEntity.ok(url);
    }
}
