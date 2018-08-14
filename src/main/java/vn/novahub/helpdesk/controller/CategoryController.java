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
import vn.novahub.helpdesk.service.*;

@RestController
@RequestMapping(path = "/api/categories")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private AccountSkillService accountSkillService;

    @PreAuthorize("isAuthenticated()")
    @GetMapping(produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<Page<Category>> getAll(@RequestParam(value = "keyword", required = false, defaultValue = "") String keyword,
                                                 Pageable pageable) {
        Page<Category> categoryPage = categoryService.getAllByName(keyword, pageable);

        return new ResponseEntity<>(categoryPage, HttpStatus.OK);
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping(path = "/{id}", produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<Category> get(@PathVariable("id") long categoryId) throws CategoryNotFoundException {
        Category category = categoryService.get(categoryId);

        return new ResponseEntity<>(category, HttpStatus.OK);
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping(path = "/{id}/skills", produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<Page<Skill>> getAllSkillsByCategoryId(@PathVariable("id") long categoryId,
                                                                @RequestParam(value = "keyword", required = false, defaultValue = "") String keyword,
                                                                Pageable pageable) throws CategoryNotFoundException {
        Page<Skill> skillPage = accountSkillService.getAllByCategoryIdAndName(categoryId, keyword, pageable);

        return new ResponseEntity<>(skillPage, HttpStatus.OK);
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping(path = "/{id}/skills/{skill_id}", produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<Skill> getASkill(@PathVariable("id") long categoryId,
                                           @PathVariable("skill_id") long skillId) throws SkillNotFoundException {
        Skill skill = accountSkillService.getByCategoryIdAndSkillId(categoryId, skillId);

        return  new ResponseEntity<>(skill, HttpStatus.OK);
    }
}
