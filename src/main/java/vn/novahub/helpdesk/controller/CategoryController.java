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
import vn.novahub.helpdesk.exception.*;
import vn.novahub.helpdesk.model.Category;
import vn.novahub.helpdesk.model.Skill;
import vn.novahub.helpdesk.service.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping(path = "/api")
public class CategoryController {

    private static final Logger logger = LoggerFactory.getLogger(CategoryController.class);

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private AccountSkillService accountSkillService;

    @Autowired
    private AdminSkillService adminSkillService;

    @Autowired
    private LogService logService;

    @PreAuthorize("isAuthenticated()")
    @GetMapping(path = "/categories", produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<Page<Category>> getAll(@RequestParam(value = "keyword", required = false, defaultValue = "") String keyword,
                                                 Pageable pageable,
                                                 HttpServletRequest request){
        logService.log(request, logger);
        Page<Category> categoryPage = categoryService.getAllByName(keyword, pageable);

        return new ResponseEntity<>(categoryPage, HttpStatus.OK);
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping(path = "/categories/{id}", produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<Category> get(@PathVariable("id") long categoryId,
                                        HttpServletRequest request) throws CategoryNotFoundException {
        logService.log(request, logger);
        Category category = categoryService.get(categoryId);

        return new ResponseEntity<>(category, HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping(path = "/categories", produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<Category> create(@RequestBody Category category,
                                           HttpServletRequest request) throws CategoryIsExistException, CategoryValidationException {
        logService.log(request, logger);
        Category newCategory = categoryService.create(category);

        return new ResponseEntity<>(newCategory, HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PutMapping(path = "/categories/{id}", produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<Category> update(@PathVariable("id") long categoryId,
                                           @RequestBody Category category,
                                           HttpServletRequest request) throws CategoryValidationException, CategoryIsExistException, CategoryNotFoundException {
        logService.log(request, logger);
        Category categoryUpdated = categoryService.update(category, categoryId);

        return new ResponseEntity<>(categoryUpdated, HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @DeleteMapping(path = "/categories/{id}", produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<Void> delete(@PathVariable("id") long categoryId,
                                       HttpServletRequest request) throws CategoryNotFoundException {
        logService.log(request, logger);
        categoryService.delete(categoryId);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping(path = "/categories/{id}/skills", produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<Page<Skill>> getAllSkillsByCategoryId(@PathVariable("id") long categoryId,
                                                                @RequestParam(value = "keyword", required = false, defaultValue = "") String keyword,
                                                                Pageable pageable,
                                                                HttpServletRequest request) throws CategoryNotFoundException {
        logService.log(request, logger);
        Page<Skill> skillPage = accountSkillService.getAllByCategoryIdAndName(categoryId, keyword, pageable);

        return new ResponseEntity<>(skillPage, HttpStatus.OK);
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping(path = "/categories/{id}/skills/{skill_id}", produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<Skill> getASkill(@PathVariable("id") long categoryId,
                                           @PathVariable("skill_id") long skillId,
                                           HttpServletRequest request) throws SkillNotFoundException {
        logService.log(request, logger);
        Skill skill = accountSkillService.getByCategoryIdAndSkillId(categoryId, skillId);

        return  new ResponseEntity<>(skill, HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping(path = "/categories/{id}/skills", produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<Skill> createASkill(@PathVariable("id") long categoryId,
                                              @RequestBody Skill skill,
                                              HttpServletRequest request) throws SkillIsExistException, SkillValidationException, CategoryNotFoundException {
        logService.log(request, logger);
        Skill newSkill = adminSkillService.createByCategoryId(skill, categoryId);

        return new ResponseEntity<>(newSkill, HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PutMapping(path = "/categories/{id}/skills/{skill_id}", produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<Skill> updateASkill(@PathVariable("id") long categoryId,
                                              @PathVariable("skill_id") long skillId,
                                              @RequestBody Skill skill,
                                              HttpServletRequest request) throws CategoryNotFoundException, SkillNotFoundException, SkillValidationException, SkillIsExistException {
        logService.log(request, logger);
        Skill skillUpdated = adminSkillService.updateByCategoryIdAndSkillId(skill, categoryId, skillId);

        return new ResponseEntity<>(skillUpdated, HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @DeleteMapping(path = "/categories/{id}/skills/{skill_id}", produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<Void> deleteASkill(@PathVariable("id") long categoryId,
                                             @PathVariable("skill_id") long skillId,
                                             HttpServletRequest request) throws SkillNotFoundException {
        logService.log(request, logger);
        adminSkillService.deleteByCategoryIdAndSkillId(categoryId, skillId);

        return new ResponseEntity<>(HttpStatus.OK);
    }
}
