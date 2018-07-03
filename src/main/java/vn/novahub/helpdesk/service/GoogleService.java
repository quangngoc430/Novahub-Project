package vn.novahub.helpdesk.service;

import org.springframework.security.core.userdetails.UserDetails;
import vn.novahub.helpdesk.exception.EmailFormatException;
import vn.novahub.helpdesk.model.GooglePojo;

import java.io.IOException;

public interface GoogleService {

    String getToken(final String code) throws IOException;

    GooglePojo getUserInfo(final String accessToken) throws IOException, EmailFormatException;

    UserDetails buildUser(GooglePojo googlePojo, String roleName);
}
