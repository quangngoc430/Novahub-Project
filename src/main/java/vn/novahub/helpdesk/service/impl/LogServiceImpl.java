package vn.novahub.helpdesk.service.impl;

import org.slf4j.Logger;
import org.springframework.stereotype.Service;
import vn.novahub.helpdesk.service.LogService;

import javax.servlet.http.HttpServletRequest;

@Service
public class LogServiceImpl implements LogService {

    @Override
    public void log(HttpServletRequest request, Logger logger) {
        logger.info("URL : " + request.getRequestURL());
        logger.info("Method : " + request.getMethod());
        logger.info("IP : " + request.getRemoteAddr());
    }
}
