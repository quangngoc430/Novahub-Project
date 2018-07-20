package vn.novahub.helpdesk.service;

import vn.novahub.helpdesk.model.Token;

public interface TokenService {

    String generateToken(String originalString);

    boolean isTokenExpired(Token token);

    long countTimeExpired(Token token);

}
