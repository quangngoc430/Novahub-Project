package vn.novahub.helpdesk.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import vn.novahub.helpdesk.exception.AccountNotFoundException;
import vn.novahub.helpdesk.exception.CategoryNotFoundException;
import vn.novahub.helpdesk.exception.SkillNotFoundException;
import vn.novahub.helpdesk.model.Category;
import vn.novahub.helpdesk.model.Skill;
import vn.novahub.helpdesk.service.AccountSkillService;
import vn.novahub.helpdesk.service.CategoryService;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.core.Is.is;
import static org.mockito.BDDMockito.given;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CategoryController.class)
public class CategoryControllerTest extends BaseControllerTest {

    @MockBean
    private CategoryService categoryService;

    @MockBean
    private AccountSkillService accountSkillService;

    private List<Skill> skills;

    private List<Category> categories;

    private ObjectMapper objectMapper;

    @Before
    public void before() throws IOException {
        categories = convertJsonFileToObjectList(
                "seeding/categories.json",
                new TypeReference<List<Category>>(){});

        skills = convertJsonFileToObjectList(
                "seeding/skills.json",
                new TypeReference<List<Skill>>(){});

        objectMapper = new ObjectMapper();
    }

    @Test
    public void testGetAll() throws Exception {
        List<Category> categoriesExcepted = new ArrayList<>();

        for(Category category : categories) {
            if(category.getName().contains("Development")) {
                categoriesExcepted.add(category);
            }
        }

        given(categoryService.getAllByName("Development", new PageRequest(0, 20))).willReturn(new PageImpl<>(categoriesExcepted));

        mvc.perform(get("/api/categories?keyword=Development")
                    .with(user(EMAIL).password(PASSWORD))
                    .with(csrf().asHeader())
                    .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.totalElements", is(3)));
    }

    @Test
    public void testGet() throws Exception {
        given(categoryService.get(1L)).willReturn(categories.get(0));

        mvc.perform(get("/api/categories/1")
                    .with(user(EMAIL).password(PASSWORD))
                    .with(csrf().asHeader())
                    .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.name", is(categories.get(0).getName())));
    }

    @Test
    public void testGetAllSkillsByCategoryId() throws Exception {
        List<Skill> skillsExpected = new ArrayList<>();

        for(Skill skill : skills) {
            if(skill.getCategoryId() == 1) {
                skillsExpected.add(skill);
            }
        }

        given(accountSkillService.getAllByCategoryIdAndName(1L, "", new PageRequest(0, 20))).willReturn(new PageImpl<>(skillsExpected, new PageRequest(0, 20), skillsExpected.size()));

        mvc.perform(get("/api/categories/1/skills")
                    .with(user(EMAIL).password(PASSWORD))
                    .with(csrf().asHeader())
                    .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.content[0].name", is(skillsExpected.get(0).getName())));
    }

    @Test
    public void testGetASkill() throws Exception {
        given(accountSkillService.getByCategoryIdAndSkillId(1L, 1L)).willReturn(skills.get(0));

        mvc.perform(get("/api/categories/1/skills/1")
                    .with(user(EMAIL).password(PASSWORD))
                    .with(csrf().asHeader())
                    .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.name", is(skills.get(0).getName())));
    }

}
