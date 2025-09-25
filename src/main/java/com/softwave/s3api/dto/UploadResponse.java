package com.softwave.s3api.dto;

public class UploadResponse {
    private String key;
    private String fileName;
    private String message;
    private String url;

    public UploadResponse() {}

    public UploadResponse(String key, String fileName, String message, String url) {
        this.key = key;
        this.fileName = fileName;
        this.message = message;
        this.url = url;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}