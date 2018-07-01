package vn.novahub.helpdesk.model;


import java.time.Instant;
import java.util.Map;

public class ApiError {

    private Instant timestamp;
    private int status;
    private Map<String, String> errors;
    private String message;
    private String path;

    public ApiError() {
        super();
    }

    public ApiError(int status, Map<String, String> errors, String message, String path) {
        super();
        this.status = status;
        this.errors = errors;
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

    public Map<String, String> getErrors() {
        return errors;
    }

    public void setErrors(Map<String, String> errors) {
        this.errors = errors;
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
                ", errors=" + errors +
                ", message='" + message + '\'' +
                ", path='" + path + '\'' +
                '}';
    }
}
