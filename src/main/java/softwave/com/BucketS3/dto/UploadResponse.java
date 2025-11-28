package softwave.com.BucketS3.dto;

public class UploadResponse {
    private String key;
    private String url;

    public UploadResponse() {}

    public UploadResponse(String key, String url) {
        this.key = key;
        this.url = url;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
