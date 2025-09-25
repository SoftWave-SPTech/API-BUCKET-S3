package com.softwave.s3api.dto;

import java.time.Instant;

public class DocumentInfo {
    private String fileName;
    private String key;
    private long size;
    private Instant lastModified;
    private String contentType;
    private String downloadUrl;

    public DocumentInfo() {}

    public DocumentInfo(String fileName, String key, long size, Instant lastModified, String contentType) {
        this.fileName = fileName;
        this.key = key;
        this.size = size;
        this.lastModified = lastModified;
        this.contentType = contentType;
    }

    // Getters and Setters
    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public Instant getLastModified() {
        return lastModified;
    }

    public void setLastModified(Instant lastModified) {
        this.lastModified = lastModified;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public String getDownloadUrl() {
        return downloadUrl;
    }

    public void setDownloadUrl(String downloadUrl) {
        this.downloadUrl = downloadUrl;
    }
}