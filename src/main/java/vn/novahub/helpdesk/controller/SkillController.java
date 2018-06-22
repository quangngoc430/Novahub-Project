package vn.novahub.helpdesk.controller;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vn.novahub.helpdesk.constant.ExceptionConstant;
import vn.novahub.helpdesk.exception.SkillNotFoundException;
import vn.novahub.helpdesk.service.LogService;
import vn.novahub.helpdesk.service.SkillService;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping(path = "/api")
public class SkillController {

    private static final Logger logger = LoggerFactory.getLogger(SkillController.class);

}
