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
import vn.novahub.helpdesk.constant.ExceptionConstant;
import vn.novahub.helpdesk.constant.ResponseConstant;
import vn.novahub.helpdesk.exception.CategoryNotFoundException;
import vn.novahub.helpdesk.exception.SkillNotFoundException;
import vn.novahub.helpdesk.model.Category;
import vn.novahub.helpdesk.model.ResponseObject;
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

    @ExceptionHandler(CategoryNotFoundException.class)
    public ResponseEntity<ResponseObject> handleCategoryNotFoundException(HttpServletRequest request, Exception ex){
        ResponseObject responseObject = new ResponseObject();
        responseObject.setCode(ExceptionConstant.CODE_CATEGORY_IS_NOT_EXIST);
        responseObject.setData(ExceptionConstant.MESSAGE_CATEGORY_IS_NOT_EXIST);

        return new ResponseEntity<ResponseObject>(responseObject, HttpStatus.OK);
    }

    @GetMapping(path = "/categories", produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<ResponseObject> getAll(@RequestParam(value = "keyword", required = false, defaultValue = "") String keyword,
                                                 Pageable pageable,
                                                 HttpServletRequest request){
        logService.log(request, logger);
        Page<Category> categoryPage = categoryService.getAllByName(keyword, pageable, request);

        ResponseObject responseObject = new ResponseObject();
        responseObject.setCode(ResponseConstant.OK);
        responseObject.setData(categoryPage);

        return new ResponseEntity<ResponseObject>(responseObject, HttpStatus.OK);
    }

    @GetMapping(path = "/categories/{id}", produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<ResponseObject> get(@PathVariable("id") long categoryId,
                                              HttpServletRequest request) throws CategoryNotFoundException {
        logService.log(request, logger);
        Category category = categoryService.get(categoryId, request);

        ResponseObject responseObject = new ResponseObject();
        responseObject.setCode(ResponseConstant.OK);
        responseObject.setData(category);

        return new ResponseEntity<ResponseObject>(responseObject, HttpStatus.OK);
    }

    @GetMapping(path = "/categories/{id}/skills", produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<ResponseObject> getAllSkillsOfACategory(@PathVariable("id") long categoryId,
                                                                  @RequestParam(value = "keyword", required = false, defaultValue = "") String keyword,
                                                                  Pageable pageable,
                                                                  HttpServletRequest request) throws CategoryNotFoundException {
        logService.log(request, logger);
        Page<Skill> skillPage = skillService.getAllByCategoryIdAndName(categoryId, keyword, pageable, request);

        ResponseObject responseObject = new ResponseObject();
        responseObject.setCode(ResponseConstant.OK);
        responseObject.setData(skillPage);

        return new ResponseEntity<ResponseObject>(responseObject, HttpStatus.OK);
    }

    @GetMapping(path = "/categories/{id}/skills/{skill_id}", produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<ResponseObject> getASkill(@PathVariable("id") long categoryId,
                                                    @PathVariable("skill_id") long skillId,
                                                    HttpServletRequest request){
        logService.log(request, logger);
        Skill skill = skillService.getByCategoryIdAndSkillId(categoryId, skillId, request);

        ResponseObject responseObject = new ResponseObject();
        responseObject.setCode(ResponseConstant.OK);
        responseObject.setData(skill);

        return  new ResponseEntity<ResponseObject>(responseObject, HttpStatus.OK);
    }

    @PostMapping(path = "/categories/{id}/skills", produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<ResponseObject> createASkill(@PathVariable("id") long categoryId,
                                                       @RequestBody Skill skill,
                                                       HttpServletRequest request){
        logService.log(request, logger);
        Skill newSkill = skillService.createByCategoryId(skill, categoryId, request);

        ResponseObject responseObject = new ResponseObject();
        responseObject.setCode(ResponseConstant.OK);
        responseObject.setData(newSkill);

        return new ResponseEntity<ResponseObject>(responseObject, HttpStatus.OK);
    }

    @PutMapping(path = "/categories/{id}/skills/{skill_id}", produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<ResponseObject> updateASkill(@PathVariable("id") long categoryId,
                                                       @PathVariable("skill_id") long skillId,
                                                       @RequestBody Skill skill,
                                                       HttpServletRequest request) throws CategoryNotFoundException, SkillNotFoundException {
        logService.log(request, logger);
        Skill skillUpdated = skillService.updateByCategoryIdAndSkillId(skill, categoryId, skillId, request);

        ResponseObject responseObject = new ResponseObject();
        responseObject.setCode(ResponseConstant.OK);
        responseObject.setData(skillUpdated);

        return new ResponseEntity<ResponseObject>(responseObject, HttpStatus.OK);
    }

    @DeleteMapping(path = "/categories/{id}/skills/{skill_id}", produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<ResponseObject> deleteASkill(@PathVariable("id") long categoryId,
                                                       @PathVariable("skill_id") long skillId,
                                                       HttpServletRequest request) throws CategoryNotFoundException, SkillNotFoundException {
        logService.log(request, logger);
        skillService.deteleByCategoryIdAndSkillId(categoryId, skillId, request);

        ResponseObject responseObject = new ResponseObject();
        responseObject.setCode(ResponseConstant.OK);

        return new ResponseEntity<ResponseObject>(responseObject, HttpStatus.OK);
    }
}
