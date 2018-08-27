package vn.novahub.helpdesk.exception;

public class BadRequestException extends Exception {

    public BadRequestException(String message) {
        super("BadRequestException with message = " + message);
    }
}
