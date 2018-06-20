package vn.novahub.helpdesk.controller;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
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

    @GetMapping(path = "/skills", produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<?> getAll(@RequestParam(value = "keyword", required = false, defaultValue = "") String keyword,
                                          HttpServletRequest request){
        logService.log(request, logger);
        return new ResponseEntity<>(skillService.getAllSkillsOfAnAccount(keyword, request), HttpStatus.OK);
    }

}
