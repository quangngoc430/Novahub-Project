package vn.novahub.helpdesk.controller;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import vn.novahub.helpdesk.exception.SkillIsExistException;
import vn.novahub.helpdesk.exception.SkillNotFoundException;
import vn.novahub.helpdesk.exception.SkillValidationException;
import vn.novahub.helpdesk.model.Account;
import vn.novahub.helpdesk.model.Skill;
import vn.novahub.helpdesk.service.LogService;
import vn.novahub.helpdesk.service.SkillService;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping(path = "/api")
public class SkillController {

    @Autowired
    private LogService logService;

    @Autowired
    private SkillService skillService;

    private static final Logger logger = LoggerFactory.getLogger(SkillController.class);

    @PreAuthorize("isAuthenticated()")
    @GetMapping(path = "/skills", produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<Page<Skill>> getAll(@RequestParam(value = "keyword", required = false, defaultValue = "") String keyword,
                                              HttpServletRequest request,
                                              Pageable pageable){
        logService.log(request, logger);

        return new ResponseEntity<>(skillService.getAllByKeyword(keyword, pageable), HttpStatus.OK);
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping(path = "/skills/{id}", produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<Skill> getAll(@PathVariable("id") long skillId,
                                              HttpServletRequest request) throws SkillNotFoundException {
        logService.log(request, logger);

        return new ResponseEntity<>(skillService.getById(skillId), HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping(path = "/skills", produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<Skill> createByAdmin(@RequestBody Skill skill,
                                               HttpServletRequest request) throws SkillIsExistException, SkillValidationException {
        logService.log(request, logger);

        return new ResponseEntity<>(skillService.createByAdmin(skill), HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PutMapping(path = "/skills/{id}", produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<Skill> updateByAdmin(@PathVariable("id") long skillId,
                                               @RequestBody Skill skill,
                                               HttpServletRequest request) throws SkillIsExistException, SkillValidationException, SkillNotFoundException {
        logService.log(request, logger);

        return new ResponseEntity<>(skillService.updateByAdmin(skillId, skill), HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @DeleteMapping(path = "/skills/{id}", produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<Void> deleteByAdmin(@PathVariable("id") long skillId,
                                              HttpServletRequest request) throws SkillNotFoundException {
        logService.log(request, logger);

        skillService.deleteByAdmin(skillId);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping(path = "/skills/{id}/users", produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<Page<Account>> getAllUsersOfASkill(@PathVariable("id") long skillId,
                                                             HttpServletRequest request,
                                                             Pageable pageable) throws SkillNotFoundException {
        logService.log(request, logger);

        return new ResponseEntity<>(skillService.getAllUsersOfASkill(skillId, pageable), HttpStatus.OK);
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping(path = "/users/me/skills", produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<Page<Skill>> getAllForAccountLogin(@RequestParam(value = "keyword", required = false, defaultValue = "") String keyword,
                                                             HttpServletRequest request,
                                                             Pageable pageable){
        logService.log(request, logger);

        return new ResponseEntity<>(skillService.getAllByKeywordForAccountLogin(keyword, pageable), HttpStatus.OK);
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping(path = "/users/me/skills", produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<Skill> createForAccountLogin(@RequestBody Skill skill,
                                                       HttpServletRequest request) throws SkillIsExistException, SkillValidationException {
        logService.log(request, logger);

        return new ResponseEntity<>(skillService.createForAccountLogin(skill), HttpStatus.OK);
    }

    @PreAuthorize("isAuthenticated()")
    @PutMapping(path = "/users/me/skills/{id}", produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<Skill> updateForAccountLogin(@PathVariable("id") long skillId,
                                                       @RequestBody Skill skill,
                                                       HttpServletRequest request) throws SkillValidationException, SkillNotFoundException {
        logService.log(request, logger);

        return new ResponseEntity<>(skillService.updateForAccountLogin(skillId, skill), HttpStatus.OK);
    }

    @PreAuthorize("isAuthenticated()")
    @DeleteMapping(path = "/users/me/skills/{id}", produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<Void> deleteForAccountLogin(@PathVariable("id") long skillId,
                                                      HttpServletRequest request) throws SkillNotFoundException {
        logService.log(request, logger);

        skillService.deleteForAccountLogin(skillId);

        return new ResponseEntity<>(HttpStatus.OK);
    }
}
