package vn.novahub.helpdesk.service;

import org.slf4j.Logger;

import javax.servlet.http.HttpServletRequest;

public interface LogService {

    void log(HttpServletRequest request, Logger logger);
}
