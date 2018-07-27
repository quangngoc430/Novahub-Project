package vn.novahub.helpdesk.exception;

public class UnauthorizedException extends Exception {

    private String url;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public UnauthorizedException(String message) {
        super(message);
    }

    public UnauthorizedException(String message, String url) {
        super(message);
        this.url = url;
    }

}
