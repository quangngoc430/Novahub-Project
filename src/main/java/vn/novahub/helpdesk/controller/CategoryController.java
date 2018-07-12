package vn.novahub.helpdesk.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import vn.novahub.helpdesk.exception.*;
import vn.novahub.helpdesk.model.Category;
import vn.novahub.helpdesk.model.Skill;
import vn.novahub.helpdesk.service.CategoryService;
import vn.novahub.helpdesk.service.SkillService;

@RestController
@RequestMapping(path = "/api")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private SkillService skillService;

    @PreAuthorize("isAuthenticated()")
    @GetMapping(path = "/categories", produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<Page<Category>> getAll(@RequestParam(value = "keyword", required = false, defaultValue = "") String keyword,
                                                 Pageable pageable){
        Page<Category> categoryPage = categoryService.getAllByName(keyword, pageable);

        return new ResponseEntity<>(categoryPage, HttpStatus.OK);
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping(path = "/categories/{id}", produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<Category> get(@PathVariable("id") long categoryId) throws CategoryNotFoundException {
        Category category = categoryService.get(categoryId);

        return new ResponseEntity<>(category, HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping(path = "/categories", produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<Category> create(@RequestBody Category category) throws CategoryIsExistException, CategoryValidationException {
        Category newCategory = categoryService.create(category);

        return new ResponseEntity<>(newCategory, HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PutMapping(path = "/categories/{id}", produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<Category> update(@PathVariable("id") long categoryId,
                                           @RequestBody Category category) throws CategoryValidationException, CategoryIsExistException, CategoryNotFoundException {
        Category categoryUpdated = categoryService.update(category, categoryId);

        return new ResponseEntity<>(categoryUpdated, HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @DeleteMapping(path = "/categories/{id}", produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<Void> delete(@PathVariable("id") long categoryId) throws CategoryNotFoundException {
        categoryService.delete(categoryId);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping(path = "/categories/{id}/skills", produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<Page<Skill>> getAllSkillsOfACategory(@PathVariable("id") long categoryId,
                                                               @RequestParam(value = "keyword", required = false, defaultValue = "") String keyword,
                                                               Pageable pageable) throws CategoryNotFoundException {
        Page<Skill> skillPage = skillService.getAllByCategoryIdAndName(categoryId, keyword, pageable);

        return new ResponseEntity<>(skillPage, HttpStatus.OK);
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping(path = "/categories/{id}/skills/{skill_id}", produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<Skill> getASkill(@PathVariable("id") long categoryId,
                                           @PathVariable("skill_id") long skillId) throws SkillNotFoundException {
        Skill skill = skillService.getByCategoryIdAndSkillId(categoryId, skillId);

        return  new ResponseEntity<>(skill, HttpStatus.OK);
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping(path = "/categories/{id}/skills", produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<Skill> createASkill(@PathVariable("id") long categoryId,
                                              @RequestBody Skill skill) throws SkillIsExistException, SkillValidationException, CategoryNotFoundException {
        Skill newSkill = skillService.createByCategoryId(skill, categoryId);

        return new ResponseEntity<>(newSkill, HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PutMapping(path = "/categories/{id}/skills/{skill_id}", produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<Skill> updateASkill(@PathVariable("id") long categoryId,
                                              @PathVariable("skill_id") long skillId,
                                              @RequestBody Skill skill) throws CategoryNotFoundException, SkillNotFoundException, SkillIsExistException, SkillValidationException {
        Skill skillUpdated = skillService.updateByCategoryIdAndSkillId(skill, categoryId, skillId);

        return new ResponseEntity<>(skillUpdated, HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @DeleteMapping(path = "/categories/{id}/skills/{skill_id}", produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<Void> deleteASkill(@PathVariable("id") long categoryId,
                                             @PathVariable("skill_id") long skillId) throws SkillNotFoundException {
        skillService.deleteByCategoryIdAndSkillId(categoryId, skillId);

        return new ResponseEntity<>(HttpStatus.OK);
    }
}
