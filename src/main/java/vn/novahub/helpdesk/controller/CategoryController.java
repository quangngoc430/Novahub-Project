package vn.novahub.helpdesk.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
import vn.novahub.helpdesk.service.LogService;
import vn.novahub.helpdesk.service.SkillService;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping(path = "/api")
public class CategoryController {

    private static final Logger logger = LoggerFactory.getLogger(CategoryController.class);

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private SkillService skillService;

    @Autowired
    private LogService logService;

    @GetMapping(path = "/categories", produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<Page<Category>> getAll(@RequestParam(value = "keyword", required = false, defaultValue = "") String keyword,
                                                 Pageable pageable,
                                                 HttpServletRequest request){
        logService.log(request, logger);
        Page<Category> categoryPage = categoryService.getAllByName(keyword, pageable, request);

        return new ResponseEntity<>(categoryPage, HttpStatus.OK);
    }

    @GetMapping(path = "/categories/{id}", produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<Category> get(@PathVariable("id") long categoryId,
                                        HttpServletRequest request) throws CategoryNotFoundException {
        logService.log(request, logger);
        Category category = categoryService.get(categoryId, request);

        return new ResponseEntity<>(category, HttpStatus.OK);
    }

    @GetMapping(path = "/categories/{id}/skills", produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<Page<Skill>> getAllSkillsOfACategory(@PathVariable("id") long categoryId,
                                                               @RequestParam(value = "keyword", required = false, defaultValue = "") String keyword,
                                                               Pageable pageable,
                                                               HttpServletRequest request) throws CategoryNotFoundException {
        logService.log(request, logger);
        Page<Skill> skillPage = skillService.getAllByCategoryIdAndName(categoryId, keyword, pageable, request);

        return new ResponseEntity<>(skillPage, HttpStatus.OK);
    }

    @GetMapping(path = "/categories/{id}/skills/{skill_id}", produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<Skill> getASkill(@PathVariable("id") long categoryId,
                                           @PathVariable("skill_id") long skillId,
                                           HttpServletRequest request){
        logService.log(request, logger);
        Skill skill = skillService.getByCategoryIdAndSkillId(categoryId, skillId, request);

        return  new ResponseEntity<>(skill, HttpStatus.OK);
    }

    @PostMapping(path = "/categories/{id}/skills", produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<Skill> createASkill(@PathVariable("id") long categoryId,
                                              @RequestBody Skill skill,
                                              HttpServletRequest request){
        logService.log(request, logger);
        Skill newSkill = skillService.createByCategoryId(skill, categoryId, request);

        return new ResponseEntity<>(newSkill, HttpStatus.OK);
    }

    @PutMapping(path = "/categories/{id}/skills/{skill_id}", produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<Skill> updateASkill(@PathVariable("id") long categoryId,
                                              @PathVariable("skill_id") long skillId,
                                              @RequestBody Skill skill,
                                              HttpServletRequest request) throws CategoryNotFoundException, SkillNotFoundException {
        logService.log(request, logger);
        Skill skillUpdated = skillService.updateByCategoryIdAndSkillId(skill, categoryId, skillId, request);

        return new ResponseEntity<>(skillUpdated, HttpStatus.OK);
    }

    @DeleteMapping(path = "/categories/{id}/skills/{skill_id}", produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<Void> deleteASkill(@PathVariable("id") long categoryId,
                                             @PathVariable("skill_id") long skillId,
                                             HttpServletRequest request) throws CategoryNotFoundException, SkillNotFoundException {
        logService.log(request, logger);
        skillService.deteleByCategoryIdAndSkillId(categoryId, skillId, request);

        return new ResponseEntity<>(HttpStatus.OK);
    }
}
