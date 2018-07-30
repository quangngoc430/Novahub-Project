package vn.novahub.helpdesk.controller;

import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;

import vn.novahub.helpdesk.exception.*;
import vn.novahub.helpdesk.model.Account;
import vn.novahub.helpdesk.model.Skill;
import vn.novahub.helpdesk.service.AccountSkillService;
import vn.novahub.helpdesk.service.AdminSkillService;

@RestController
@RequestMapping(path = "/api")
public class SkillController {

    @Autowired
    private AccountSkillService accountSkillService;

    @Autowired
    private AdminSkillService adminSkillService;

    @PreAuthorize("isAuthenticated()")
    @GetMapping(path = "/skills", produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<Page<Skill>> getAll(@RequestParam(value = "keyword", required = false, defaultValue = "") String keyword,
                                              Pageable pageable) {
        return new ResponseEntity<>(accountSkillService.getAllByKeyword(keyword, pageable), HttpStatus.OK);
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping(path = "/skills/{id}", produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<Skill> getById(@PathVariable("id") long skillId) throws SkillNotFoundException {
        return new ResponseEntity<>(adminSkillService.findOne(skillId), HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping(path = "/skills", produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<Skill> createByAdmin(@RequestBody Skill skill) throws SkillIsExistException, SkillValidationException, CategoryNotFoundException {
        return new ResponseEntity<>(adminSkillService.create(skill), HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PutMapping(path = "/skills/{id}", produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<Skill> updateByAdmin(@PathVariable("id") long skillId,
                                               @RequestBody Skill skill) throws SkillIsExistException, SkillValidationException, SkillNotFoundException, CategoryNotFoundException {
        return new ResponseEntity<>(adminSkillService.update(skillId, skill), HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @DeleteMapping(path = "/skills/{id}", produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<Void> deleteByAdmin(@PathVariable("id") long skillId) throws SkillNotFoundException {
        adminSkillService.delete(skillId);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping(path = "/skills/{id}/users", produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<Page<Account>> getAllUsersBySkillId(@PathVariable("id") long skillId,
                                                              Pageable pageable) throws SkillNotFoundException {
        return new ResponseEntity<>(accountSkillService.getAllUsersBySkillId(skillId, pageable), HttpStatus.OK);
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping(path = "/users/me/skills", produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<Page<Skill>> getAllByAccountLogin(@RequestParam(value = "keyword", required = false, defaultValue = "") String keyword,
                                                             Pageable pageable) {
        return new ResponseEntity<>(accountSkillService.getAllByKeywordForAccountLogin(keyword, pageable), HttpStatus.OK);
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping(path = "/users/me/skills", produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<Skill> create(@RequestBody Skill skill) throws SkillIsExistException, SkillValidationException, CategoryNotFoundException, LevelValidationException {
        return new ResponseEntity<>(accountSkillService.create(skill), HttpStatus.OK);
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping(path = "/users/me/skills/{id}", produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<Skill> get(@PathVariable("id") long skillId) throws SkillNotFoundException {
        return new ResponseEntity<>(accountSkillService.findOne(skillId), HttpStatus.OK);
    }

    @PreAuthorize("isAuthenticated()")
    @PutMapping(path = "/users/me/skills/{id}", produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<Skill> update(@PathVariable("id") long skillId,
                                        @RequestBody Skill skill) throws SkillNotFoundException, LevelValidationException, SkillValidationException {
        return new ResponseEntity<>(accountSkillService.update(skillId, skill), HttpStatus.OK);
    }

    @PreAuthorize("isAuthenticated()")
    @DeleteMapping(path = "/users/me/skills/{id}", produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<Void> delete(@PathVariable("id") long skillId) throws SkillNotFoundException {
        accountSkillService.delete(skillId);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping(path = "/users/{id}/skills", produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<Page<Skill>> getAllByAccountId(@PathVariable("id") long accountId,
                                                         Pageable pageable) throws AccountNotFoundException {
        return new ResponseEntity<>(accountSkillService.getAllByAccountId(accountId, pageable), HttpStatus.OK);
    }
}
