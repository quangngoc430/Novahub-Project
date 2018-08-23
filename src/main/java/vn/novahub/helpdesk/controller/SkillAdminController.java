package vn.novahub.helpdesk.controller;

import com.fasterxml.jackson.annotation.JsonView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import vn.novahub.helpdesk.exception.CategoryNotFoundException;
import vn.novahub.helpdesk.exception.SkillIsExistException;
import vn.novahub.helpdesk.exception.SkillNotFoundException;
import vn.novahub.helpdesk.exception.SkillValidationException;
import vn.novahub.helpdesk.model.Skill;
import vn.novahub.helpdesk.service.AdminSkillService;
import vn.novahub.helpdesk.view.View;

@RestController
@RequestMapping(path = "/api/admin/skills")
public class SkillAdminController {

    @Autowired
    private AdminSkillService adminSkillService;

    @JsonView(View.Public.class)
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping(produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<Skill> createByAdmin(@RequestBody Skill skill) throws SkillIsExistException, SkillValidationException, CategoryNotFoundException {
        return new ResponseEntity<>(adminSkillService.create(skill), HttpStatus.OK);
    }

    @JsonView(View.Public.class)
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PutMapping(path = "/{id}", produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<Skill> updateByAdmin(@PathVariable("id") long skillId,
                                               @RequestBody Skill skill) throws SkillIsExistException, SkillValidationException, SkillNotFoundException, CategoryNotFoundException {
        return new ResponseEntity<>(adminSkillService.update(skillId, skill), HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @DeleteMapping(path = "/{id}", produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<Void> deleteByAdmin(@PathVariable("id") long skillId) throws SkillNotFoundException {
        adminSkillService.delete(skillId);

        return new ResponseEntity<>(HttpStatus.OK);
    }
}
