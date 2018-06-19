package vn.novahub.helpdesk.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import vn.novahub.helpdesk.service.SkillService;

import javax.servlet.http.HttpServletRequest;

@RestController
public class SkillController {

    @Autowired
    private SkillService skillService;

    @GetMapping(value = "/api/skills", produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<?> getAllSkills(@RequestParam(value = "keyword", required = false, defaultValue = "") String keyword,
                                          HttpServletRequest request){
        return new ResponseEntity<>(skillService.getAllSkillsOfAnAccount(keyword, request), HttpStatus.OK);
    }

}
