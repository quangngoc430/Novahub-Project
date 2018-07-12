package vn.novahub.helpdesk.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vn.novahub.helpdesk.exception.CategoryNotFoundException;
import vn.novahub.helpdesk.exception.SkillNotFoundException;
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

    @GetMapping(path = "/categories", produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<Page<Category>> getAll(@RequestParam(value = "keyword", required = false, defaultValue = "") String keyword,
                                                 Pageable pageable){
        Page<Category> categoryPage = categoryService.getAllByName(keyword, pageable);

        return new ResponseEntity<>(categoryPage, HttpStatus.OK);
    }

    @GetMapping(path = "/categories/{id}", produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<Category> get(@PathVariable("id") long categoryId) throws CategoryNotFoundException {
        Category category = categoryService.get(categoryId);

        return new ResponseEntity<>(category, HttpStatus.OK);
    }

    @GetMapping(path = "/categories/{id}/skills", produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<Page<Skill>> getAllSkillsOfACategory(@PathVariable("id") long categoryId,
                                                               @RequestParam(value = "keyword", required = false, defaultValue = "") String keyword,
                                                               Pageable pageable) throws CategoryNotFoundException {
        Page<Skill> skillPage = skillService.getAllByCategoryIdAndName(categoryId, keyword, pageable);

        return new ResponseEntity<>(skillPage, HttpStatus.OK);
    }

    @GetMapping(path = "/categories/{id}/skills/{skill_id}", produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<Skill> getASkill(@PathVariable("id") long categoryId,
                                           @PathVariable("skill_id") long skillId){
        Skill skill = skillService.getByCategoryIdAndSkillId(categoryId, skillId);

        return  new ResponseEntity<>(skill, HttpStatus.OK);
    }

    @PostMapping(path = "/categories/{id}/skills", produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<Skill> createASkill(@PathVariable("id") long categoryId,
                                              @RequestBody Skill skill){
        Skill newSkill = skillService.createByCategoryId(skill, categoryId);

        return new ResponseEntity<>(newSkill, HttpStatus.OK);
    }

    @PutMapping(path = "/categories/{id}/skills/{skill_id}", produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<Skill> updateASkill(@PathVariable("id") long categoryId,
                                              @PathVariable("skill_id") long skillId,
                                              @RequestBody Skill skill) throws CategoryNotFoundException, SkillNotFoundException {
        Skill skillUpdated = skillService.updateByCategoryIdAndSkillId(skill, categoryId, skillId);

        return new ResponseEntity<>(skillUpdated, HttpStatus.OK);
    }

    @DeleteMapping(path = "/categories/{id}/skills/{skill_id}", produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<Void> deleteASkill(@PathVariable("id") long categoryId,
                                             @PathVariable("skill_id") long skillId) throws CategoryNotFoundException, SkillNotFoundException {
        skillService.deteleByCategoryIdAndSkillId(categoryId, skillId);

        return new ResponseEntity<>(HttpStatus.OK);
    }
}
