package vn.novahub.helpdesk.service;

import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.stereotype.Service;
import vn.novahub.helpdesk.model.Token;

import java.util.Date;

@Service
public class TokenServiceImpl implements TokenService {

    @Override
    public String generateToken(String originalString) {
        return DigestUtils.sha256Hex(originalString);
    }

    @Override
    public boolean isTokenExpired(Token token) {
        long createdAt = token.getCreatedAt().getTime();
        long time = token.getTime() * 1000;
        long now = (new Date()).getTime();

        return (now - createdAt) > time;
    }

}
