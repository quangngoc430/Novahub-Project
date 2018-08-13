package vn.novahub.helpdesk.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import vn.novahub.helpdesk.exception.*;
import vn.novahub.helpdesk.model.Category;
import vn.novahub.helpdesk.model.Skill;
import vn.novahub.helpdesk.service.AdminSkillService;
import vn.novahub.helpdesk.service.CategoryService;

@RestController
@RequestMapping(path = "/api/admin/categories")
public class CategoryAdminController {

    @Autowired
    private AdminSkillService adminSkillService;

    @Autowired
    private CategoryService categoryService;

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping(produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<Category> create(@RequestBody Category category) throws CategoryIsExistException, CategoryValidationException {
        Category newCategory = categoryService.create(category);

        return new ResponseEntity<>(newCategory, HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PutMapping(path = "/{id}", produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<Category> update(@PathVariable("id") long categoryId,
                                           @RequestBody Category category) throws CategoryValidationException, CategoryIsExistException, CategoryNotFoundException {
        Category categoryUpdated = categoryService.update(category, categoryId);

        return new ResponseEntity<>(categoryUpdated, HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @DeleteMapping(path = "/{id}", produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<Void> delete(@PathVariable("id") long categoryId) throws CategoryNotFoundException {
        categoryService.delete(categoryId);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping(path = "/{id}/skills", produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<Skill> createASkill(@PathVariable("id") long categoryId,
                                              @RequestBody Skill skill) throws SkillIsExistException, SkillValidationException, CategoryNotFoundException {
        Skill newSkill = adminSkillService.createByCategoryId(skill, categoryId);

        return new ResponseEntity<>(newSkill, HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PutMapping(path = "/{id}/skills/{skill_id}", produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<Skill> updateASkill(@PathVariable("id") long categoryId,
                                              @PathVariable("skill_id") long skillId,
                                              @RequestBody Skill skill) throws SkillNotFoundException, SkillValidationException, SkillIsExistException {

        Skill skillUpdated = adminSkillService.updateByCategoryIdAndSkillId(skill, categoryId, skillId);

        return new ResponseEntity<>(skillUpdated, HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @DeleteMapping(path = "/{id}/skills/{skill_id}", produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<Void> deleteASkill(@PathVariable("id") long categoryId,
                                             @PathVariable("skill_id") long skillId) throws SkillNotFoundException {
        adminSkillService.deleteByCategoryIdAndSkillId(categoryId, skillId);

        return new ResponseEntity<>(HttpStatus.OK);
    }

}
