package vn.novahub.helpdesk.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vn.novahub.helpdesk.model.Category;
import vn.novahub.helpdesk.model.Skill;
import vn.novahub.helpdesk.service.CategoryService;
import vn.novahub.helpdesk.service.LogService;
import vn.novahub.helpdesk.service.SkillService;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;

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
    public ResponseEntity<?> getAll(@RequestParam(value = "keyword", required = false, defaultValue = "") String keyword,
                                    HttpServletRequest request){
        logService.log(request, logger);
        ArrayList<Category> categoryArrayList = categoryService.getAllCategories(keyword, request);
        return new ResponseEntity<>(categoryArrayList, HttpStatus.OK);
    }

    @GetMapping(path = "/categories/{id}", produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<?> get(@PathVariable("id") long categoryId,
                                 HttpServletRequest request){
        logService.log(request, logger);
        Category category = categoryService.getACategory(categoryId, request);
        return new ResponseEntity<>(category, HttpStatus.OK);
    }

    @GetMapping(path = "/categories/{id}/skills", produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<?> getAllOfACategory(@PathVariable("id") long categoryId,
                                                     @RequestParam(value = "keyword", required = false, defaultValue = "") String keyword,
                                                     HttpServletRequest request){
        logService.log(request, logger);
        ArrayList<Skill> skillArrayList = skillService.getAllSkillsOfACategory(categoryId, keyword, request);
        return new ResponseEntity<>(skillArrayList, HttpStatus.OK);
    }

    @GetMapping(path = "/categories/{id}/skills/{skill_id}", produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<?> get(@PathVariable("id") long categoryId,
                                                  @PathVariable("skill_id") long skillId,
                                                  HttpServletRequest request){
        logService.log(request, logger);
        Skill skill = skillService.getASkillByCategoryIdAndSkillId(categoryId, skillId, request);
        return  new ResponseEntity<>(skill, HttpStatus.OK);
    }

    @PostMapping(path = "/categories/{id}/skills", produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<?> create(@PathVariable("id") long categoryId,
                                                     @RequestBody Skill skill,
                                                     HttpServletRequest request){
        logService.log(request, logger);
        Skill newSkill = skillService.createASkillOfACategory(skill, categoryId, request);
        return new ResponseEntity<>(newSkill, HttpStatus.OK);
    }

    @PutMapping(path = "/categories/{id}/skills/{skill_id}", produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<?> update(@PathVariable("id") long categoryId,
                                                     @PathVariable("skill_id") long skillId,
                                                     @RequestBody Skill skill,
                                                     HttpServletRequest request){
        logService.log(request, logger);
        Skill newSkil = skillService.updateSkillByCategoryIdAndSkillId(skill, categoryId, skillId, request);
        return new ResponseEntity<>( newSkil, HttpStatus.OK);
    }

    @DeleteMapping(path = "/categories/{id}/skills/{skill_id}", produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<?> delete(@PathVariable("id") long categoryId,
                                                    @PathVariable("skill_id") long skillId,
                                                    HttpServletRequest request){
        logService.log(request, logger);
        skillService.deteleASkillByCategoryIdAndSkillId(categoryId, skillId, request);

        return new ResponseEntity<>( "OK", HttpStatus.OK);
    }
}
