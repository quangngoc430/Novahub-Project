package vn.novahub.helpdesk.service.impl;

import java.io.IOException;
import org.apache.http.client.HttpResponseException;
import org.apache.http.client.fluent.Request;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import vn.novahub.helpdesk.exception.EmailFormatException;
import vn.novahub.helpdesk.exception.UnauthorizedException;
import vn.novahub.helpdesk.model.GooglePojo;
import vn.novahub.helpdesk.service.GoogleService;

@Component
public class GoogleServiceImpl implements GoogleService {

    @Autowired
    private Environment env;

    public GooglePojo getUserInfo(final String accessToken) throws IOException, EmailFormatException, UnauthorizedException {
        String link = env.getProperty("google.link.get.user_info") + accessToken;
        String response = "";

        try {
            response = Request.Get(link).execute().returnContent().asString();
        } catch (HttpResponseException httpResponseException){
            throw new UnauthorizedException(httpResponseException.getMessage());
        }

        ObjectMapper mapper = new ObjectMapper();


        GooglePojo googlePojo = new GooglePojo();
        googlePojo.setEmail(mapper.readTree(response).get("email").textValue());

        if(!googlePojo.getEmail().endsWith("@novahub.vn"))
            throw new EmailFormatException(googlePojo.getEmail());

        googlePojo.setName(mapper.readTree(response).get("name").textValue());
        googlePojo.setFamilyName(mapper.readTree(response).get("family_name").textValue());
        googlePojo.setGivenName(mapper.readTree(response).get("given_name").textValue());
        googlePojo.setPicture(mapper.readTree(response).get("picture").textValue());
        googlePojo.setHd(mapper.readTree(response).get("hd").textValue());

        return googlePojo;
    }
}
