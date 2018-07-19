package vn.novahub.helpdesk.model;


import java.time.Instant;
import java.util.Map;

public class ApiError {

    private Instant timestamp;
    private int status;
    private Map<String, String> error;
    private String message;
    private String path;

    public ApiError() {
        super();
    }

    public ApiError(int status, Map<String, String> error, String message, String path) {
        super();
        this.status = status;
        this.error = error;
        this.message = message;
        this.path = path;
        this.timestamp = Instant.now();
    }

    public Instant getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Instant timestamp) {
        this.timestamp = timestamp;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public Map<String, String> getError() {
        return error;
    }

    public void setError(Map<String, String> error) {
        this.error = error;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    @Override
    public String toString() {
        return "ApiError{" +
                "timestamp=" + timestamp +
                ", status=" + status +
                ", errors=" + error +
                ", message='" + message + '\'' +
                ", path='" + path + '\'' +
                '}';
    }
}
