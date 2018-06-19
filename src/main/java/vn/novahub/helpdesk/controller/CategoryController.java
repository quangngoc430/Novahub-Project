package vn.novahub.helpdesk.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vn.novahub.helpdesk.model.Category;
import vn.novahub.helpdesk.model.Skill;
import vn.novahub.helpdesk.service.CategoryService;
import vn.novahub.helpdesk.service.SkillService;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;

@RestController
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private SkillService skillService;

    @GetMapping(value = "/api/categories", produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<?> getAllCategories(@RequestParam(value = "keyword", required = false, defaultValue = "") String keyword,
                                              HttpServletRequest request){
        ArrayList<Category> categoryArrayList = categoryService.getAllCategories(keyword, request);
        return new ResponseEntity<>(categoryArrayList, HttpStatus.OK);
    }

    @GetMapping(value = "/api/categories/{id}", produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<?> getACategory(@PathVariable("id") long categoryId,
                                          HttpServletRequest request){
        Category category = categoryService.getACategory(categoryId, request);
        return new ResponseEntity<>(category, HttpStatus.OK);
    }

    @GetMapping(value = "/api/categories/{id}/skills", produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<?> getAllSkillsOfACategory(@PathVariable("id") long categoryId,
                                                    @RequestParam(value = "keyword", required = false, defaultValue = "") String keyword,
                                                    HttpServletRequest request){
        ArrayList<Skill> skillArrayList = skillService.getAllSkillsOfACategory(categoryId, keyword, request);
        return new ResponseEntity<>(skillArrayList, HttpStatus.OK);
    }

    @GetMapping(value = "/api/categories/{id}/skills/{skill_id}", produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<?> getASkillOfACategory(@PathVariable("id") long categoryId,
                                                  @PathVariable("skill_id") long skillId,
                                                  HttpServletRequest request){
        Skill skill = skillService.getASkillByCategoryIdAndSkillId(categoryId, skillId, request);
        return  new ResponseEntity<>(skill, HttpStatus.OK);
    }

    @PostMapping(value = "/api/categories/{id}/skills", produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<?> createASkillOfACategory(@PathVariable("id") long categoryId,
                                                     @RequestBody Skill skill,
                                                     HttpServletRequest request){
        Skill newSkill = skillService.createASkillOfACategory(skill, categoryId, request);
        return new ResponseEntity<>(newSkill, HttpStatus.OK);
    }

    @PutMapping(value = "/api/categories/{id}/skills/{skill_id}", produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<?> updateASkillOfACategory(@PathVariable("id") long categoryId,
                                                     @PathVariable("skill_id") long skillId,
                                                     @RequestBody Skill skill,
                                                     HttpServletRequest request){
        Skill newSkil = skillService.updateSkillByCategoryIdAndSkillId(skill, categoryId, skillId, request);
        return new ResponseEntity<>( newSkil, HttpStatus.OK);
    }

    @DeleteMapping(value = "/api/categories/{id}/skills/{skill_id}", produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<?> deleteASkillOfCategory(@PathVariable("id") long categoryId,
                                                    @PathVariable("skill_id") long skillId,
                                                    HttpServletRequest request){
        skillService.deteleASkillByCategoryIdAndSkillId(categoryId, skillId, request);

        return new ResponseEntity<>( "OK", HttpStatus.OK);
    }
}
