package vn.novahub.helpdesk.service;

import vn.novahub.helpdesk.exception.EmailFormatException;
import vn.novahub.helpdesk.exception.UnauthorizedException;
import vn.novahub.helpdesk.model.GooglePojo;

import java.io.IOException;

public interface GoogleService {

    GooglePojo getUserInfo(final String accessToken) throws IOException, EmailFormatException, UnauthorizedException;

}
