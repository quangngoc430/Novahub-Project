package vn.novahub.helpdesk.service.impl;

import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.stereotype.Service;
import vn.novahub.helpdesk.service.TokenService;

@Service
public class TokenServiceImpl implements TokenService {

    @Override
    public String generateToken(String originalString) {
        return DigestUtils.sha256Hex(originalString);
    }

}
