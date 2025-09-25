package com.softwave.s3api.service;

import com.softwave.s3api.dto.S3ObjectInfo;
import com.softwave.s3api.exception.S3ServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.ResponseInputStream;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.*;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class S3Service {

    @Autowired
    private S3Client s3Client;

    @Value("${aws.s3.bucket-name}")
    private String bucketName;

    @Value("${aws.region}")
    private String region;

    /**
     * Upload de arquivo para o S3
     */
    public String uploadFile(MultipartFile file, String folder) {
        try {
            String fileName = generateFileName(file.getOriginalFilename());
            String key = folder != null && !folder.isEmpty() ? folder + "/" + fileName : fileName;

            PutObjectRequest putRequest = PutObjectRequest.builder()
                    .bucket(bucketName)
                    .key(key)
                    .contentType(file.getContentType())
                    .build();

            s3Client.putObject(putRequest, RequestBody.fromBytes(file.getBytes()));

            return key;

        } catch (IOException e) {
            throw new S3ServiceException("Erro ao fazer upload do arquivo: " + e.getMessage(), e);
        } catch (S3Exception e) {
            throw new S3ServiceException("Erro do S3 ao fazer upload: " + e.awsErrorDetails().errorMessage(), e);
        }
    }

    /**
     * Download de arquivo do S3
     */
    public ResponseInputStream<GetObjectResponse> downloadFile(String key) {
        try {
            GetObjectRequest getRequest = GetObjectRequest.builder()
                    .bucket(bucketName)
                    .key(key)
                    .build();

            return s3Client.getObject(getRequest);

        } catch (S3Exception e) {
            throw new S3ServiceException("Erro ao fazer download do arquivo: " + e.awsErrorDetails().errorMessage(), e);
        }
    }

    /**
     * Deletar arquivo do S3
     */
    public void deleteFile(String key) {
        try {
            DeleteObjectRequest deleteRequest = DeleteObjectRequest.builder()
                    .bucket(bucketName)
                    .key(key)
                    .build();

            s3Client.deleteObject(deleteRequest);

        } catch (S3Exception e) {
            throw new S3ServiceException("Erro ao deletar arquivo: " + e.awsErrorDetails().errorMessage(), e);
        }
    }

    /**
     * Listar arquivos do S3
     */
    public List<S3ObjectInfo> listFiles(String prefix) {
        try {
            ListObjectsV2Request.Builder requestBuilder = ListObjectsV2Request.builder()
                    .bucket(bucketName);

            if (prefix != null && !prefix.isEmpty()) {
                requestBuilder.prefix(prefix);
            }

            ListObjectsV2Response response = s3Client.listObjectsV2(requestBuilder.build());

            return response.contents().stream()
                    .map(s3Object -> new S3ObjectInfo(
                            s3Object.key(),
                            extractFileName(s3Object.key()),
                            s3Object.size(),
                            s3Object.lastModified(),
                            s3Object.eTag()
                    ))
                    .collect(Collectors.toList());

        } catch (S3Exception e) {
            throw new S3ServiceException("Erro ao listar arquivos: " + e.awsErrorDetails().errorMessage(), e);
        }
    }

    /**
     * Gerar URL pública para download (funciona apenas para buckets públicos)
     */
    public String generatePresignedUrl(String key, long expirationMinutes) {
        try {
            // Para uma URL simples (funciona apenas para buckets públicos)
            return String.format("https://%s.s3.%s.amazonaws.com/%s", bucketName, region, key);

        } catch (Exception e) {
            throw new S3ServiceException("Erro ao gerar URL: " + e.getMessage(), e);
        }
    }

    /**
     * Verificar se arquivo existe
     */
    public boolean fileExists(String key) {
        try {
            HeadObjectRequest headRequest = HeadObjectRequest.builder()
                    .bucket(bucketName)
                    .key(key)
                    .build();

            s3Client.headObject(headRequest);
            return true;

        } catch (NoSuchKeyException e) {
            return false;
        } catch (S3Exception e) {
            throw new S3ServiceException("Erro ao verificar existência do arquivo: " + e.awsErrorDetails().errorMessage(), e);
        }
    }

    /**
     * Gerar nome único para arquivo
     */
    private String generateFileName(String originalFilename) {
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
        String fileExtension = "";
        
        if (originalFilename != null && originalFilename.contains(".")) {
            fileExtension = originalFilename.substring(originalFilename.lastIndexOf("."));
        }
        
        String baseName = originalFilename != null ? 
            originalFilename.substring(0, originalFilename.lastIndexOf(".")) : "file";
        
        return URLEncoder.encode(baseName + "_" + timestamp + fileExtension, StandardCharsets.UTF_8);
    }

    /**
     * Extrair nome do arquivo da key
     */
    private String extractFileName(String key) {
        return key.contains("/") ? key.substring(key.lastIndexOf("/") + 1) : key;
    }
}