package vn.novahub.helpdesk.service;

import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.stereotype.Service;

@Service
public class TokenServiceImpl implements TokenService {

    @Override
    public String generateToken(String originalString) {
        return DigestUtils.sha256Hex(originalString);
    }

}
