package com.softwave.s3api.service;

import com.softwave.s3api.dto.DocumentInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.*;

import java.io.IOException;
import java.time.Duration;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class S3Service {

    @Autowired
    private S3Client s3Client;

    @Value("${aws.s3.bucket-name}")
    private String bucketName;

    public String uploadDocument(MultipartFile file, String folder) throws IOException {
        String key = (folder != null && !folder.isEmpty()) ? 
            folder + "/" + file.getOriginalFilename() : 
            file.getOriginalFilename();

        PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                .bucket(bucketName)
                .key(key)
                .contentType(file.getContentType())
                .build();

        s3Client.putObject(putObjectRequest, 
                RequestBody.fromInputStream(file.getInputStream(), file.getSize()));

        return key;
    }

    public List<DocumentInfo> listDocuments(String prefix) {
        ListObjectsV2Request listRequest = ListObjectsV2Request.builder()
                .bucket(bucketName)
                .prefix(prefix != null ? prefix : "")
                .build();

        ListObjectsV2Response listResponse = s3Client.listObjectsV2(listRequest);

        return listResponse.contents().stream()
                .map(this::mapToDocumentInfo)
                .collect(Collectors.toList());
    }

    public void deleteDocument(String key) {
        DeleteObjectRequest deleteRequest = DeleteObjectRequest.builder()
                .bucket(bucketName)
                .key(key)
                .build();

        s3Client.deleteObject(deleteRequest);
    }

    public String generatePresignedUrl(String key, Duration duration) {
        GetObjectRequest getObjectRequest = GetObjectRequest.builder()
                .bucket(bucketName)
                .key(key)
                .build();

        // Note: For simplicity, returning a basic URL format
        // In production, you should use S3Presigner for signed URLs
        return String.format("https://%s.s3.amazonaws.com/%s", bucketName, key);
    }

    public boolean documentExists(String key) {
        try {
            HeadObjectRequest headRequest = HeadObjectRequest.builder()
                    .bucket(bucketName)
                    .key(key)
                    .build();
            
            s3Client.headObject(headRequest);
            return true;
        } catch (NoSuchKeyException e) {
            return false;
        }
    }

    private DocumentInfo mapToDocumentInfo(S3Object s3Object) {
        String fileName = s3Object.key().substring(s3Object.key().lastIndexOf('/') + 1);
        DocumentInfo docInfo = new DocumentInfo(
                fileName,
                s3Object.key(),
                s3Object.size(),
                s3Object.lastModified(),
                "application/octet-stream" // Default content type
        );
        docInfo.setDownloadUrl(generatePresignedUrl(s3Object.key(), Duration.ofHours(1)));
        return docInfo;
    }
}