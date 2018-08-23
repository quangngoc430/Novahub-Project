package vn.novahub.helpdesk.controller;

import com.fasterxml.jackson.annotation.JsonView;
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
import vn.novahub.helpdesk.view.View;

import java.util.ArrayList;

@RestController
@RequestMapping(path = "/api/skills")
public class SkillController {

    @Autowired
    private AccountSkillService accountSkillService;

    @Autowired
    private AdminSkillService adminSkillService;

    @JsonView(View.Public.class)
    @PreAuthorize("isAuthenticated()")
    @GetMapping(produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<Page<Skill>> getAll(@RequestParam(value = "categoryId") long categoryId,
                                              @RequestParam(value = "keyword", required = false, defaultValue = "") String keyword,
                                              Pageable pageable) throws CategoryNotFoundException {
        return new ResponseEntity<>(accountSkillService.getAllByKeyword(categoryId, keyword, pageable), HttpStatus.OK);
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping(path = "/search", produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<Page<Skill>> search(Pageable pageable) {
        ArrayList<Long> skillIds = new ArrayList<>();
        skillIds.add(1l);
        skillIds.add(2l);
        skillIds.add(4l);
        return new ResponseEntity<>(accountSkillService.search(skillIds, pageable), HttpStatus.OK);
    }

    @JsonView(View.Public.class)
    @PreAuthorize("isAuthenticated()")
    @GetMapping(path = "/{id}", produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<Skill> getById(@PathVariable("id") long skillId) throws SkillNotFoundException {
        return new ResponseEntity<>(adminSkillService.findOne(skillId), HttpStatus.OK);
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping(path = "/{id}/users", produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<Page<Account>> getAllUsersBySkillId(@PathVariable("id") long skillId,
                                                              Pageable pageable) throws SkillNotFoundException {
        return new ResponseEntity<>(accountSkillService.getAllUsersBySkillId(skillId, pageable), HttpStatus.OK);
    }

    @JsonView(View.SkillWithLevel.class)
    @PreAuthorize("isAuthenticated()")
    @GetMapping(path = "/me", produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<Page<Skill>> getAllByAccountLogin(@RequestParam(value = "keyword", required = false, defaultValue = "") String keyword,
                                                            Pageable pageable) {
        return new ResponseEntity<>(accountSkillService.getAllByKeywordForAccountLogin(keyword, pageable), HttpStatus.OK);
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping(path = "/me", produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<Skill> create(@RequestBody Skill skill) throws SkillIsExistException, SkillValidationException, CategoryNotFoundException, LevelValidationException {
        return new ResponseEntity<>(accountSkillService.create(skill), HttpStatus.OK);
    }

    @JsonView(View.SkillWithLevel.class)
    @PreAuthorize("isAuthenticated()")
    @GetMapping(path = "/me/{id}", produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<Skill> get(@PathVariable("id") long skillId) throws SkillNotFoundException {
        return new ResponseEntity<>(accountSkillService.findOne(skillId), HttpStatus.OK);
    }

    @PreAuthorize("isAuthenticated()")
    @PutMapping(path = "/me/{id}", produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<Skill> update(@PathVariable("id") long skillId,
                                        @RequestBody Skill skill) throws SkillNotFoundException, LevelValidationException, SkillValidationException, SkillIsExistException {
        return new ResponseEntity<>(accountSkillService.update(skillId, skill), HttpStatus.OK);
    }

    @PreAuthorize("isAuthenticated()")
    @DeleteMapping(path = "/me/{id}", produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<Void> delete(@PathVariable("id") long skillId) throws SkillNotFoundException {
        accountSkillService.delete(skillId);

        return new ResponseEntity<>(HttpStatus.OK);
    }
}
