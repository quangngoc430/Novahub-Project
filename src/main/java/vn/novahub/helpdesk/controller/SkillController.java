package vn.novahub.helpdesk.controller;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vn.novahub.helpdesk.constant.ExceptionConstant;
import vn.novahub.helpdesk.exception.SkillNotFoundException;
import vn.novahub.helpdesk.model.ResponseObject;
import vn.novahub.helpdesk.service.LogService;
import vn.novahub.helpdesk.service.SkillService;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping(path = "/api")
public class SkillController {

    private static final Logger logger = LoggerFactory.getLogger(SkillController.class);

    @Autowired
    private SkillService skillService;

    @Autowired
    private LogService logService;

    @ExceptionHandler(SkillNotFoundException.class)
    public ResponseEntity<ResponseObject> handleSkillNotFoundException(HttpServletRequest request, Exception ex){
        ResponseObject responseObject = new ResponseObject();
        responseObject.setCode(ExceptionConstant.CODE_SKILL_IS_NOT_EXIST);
        responseObject.setData(ExceptionConstant.MESSAGE_SKILL_IS_NOT_EXIST);

        return new ResponseEntity<ResponseObject>(responseObject, HttpStatus.OK);
    }

}
