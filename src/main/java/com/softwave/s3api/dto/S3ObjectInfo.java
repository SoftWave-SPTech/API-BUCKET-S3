package com.softwave.s3api.dto;

import java.time.Instant;

public class S3ObjectInfo {
    private String key;
    private String fileName;
    private long size;
    private Instant lastModified;
    private String etag;

    public S3ObjectInfo() {}

    public S3ObjectInfo(String key, String fileName, long size, Instant lastModified, String etag) {
        this.key = key;
        this.fileName = fileName;
        this.size = size;
        this.lastModified = lastModified;
        this.etag = etag;
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

    public String getEtag() {
        return etag;
    }

    public void setEtag(String etag) {
        this.etag = etag;
    }
}