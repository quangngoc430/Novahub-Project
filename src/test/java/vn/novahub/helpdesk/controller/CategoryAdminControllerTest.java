package vn.novahub.helpdesk.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import vn.novahub.helpdesk.model.Category;
import vn.novahub.helpdesk.model.Skill;
import vn.novahub.helpdesk.service.AdminSkillService;
import vn.novahub.helpdesk.service.CategoryService;

import java.io.IOException;
import java.util.List;

import static org.hamcrest.core.Is.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CategoryAdminController.class)
public class CategoryAdminControllerTest extends BaseControllerTest {

    @MockBean
    private AdminSkillService adminSkillService;

    @MockBean
    private CategoryService categoryService;

    private List<Category> categories;

    private List<Skill> skills;

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
    public void testCreate() throws Exception {
        Category category = new Category();
        category.setName("Mobile Development");

        given(categoryService.create(any(Category.class))).willReturn(categories.get(6));

        mvc.perform(post("/api/admin/categories")
                    .with(user(EMAIL).password(PASSWORD))
                    .with(csrf().asHeader())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(category)))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.name", is(categories.get(6).getName())));
    }

    @Test
    public void testUpdate() throws Exception {
        Category category = new Category();
        category.setName("ABC");

        given(categoryService.update(any(Category.class), anyLong())).willReturn(category);

        mvc.perform(put("/api/admin/categories/1")
                    .with(user(EMAIL).password(PASSWORD))
                    .with(csrf().asHeader())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(category)))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.name", is("ABC")));
    }

    @Test
    public void testDelete() throws Exception {
        mvc.perform(delete("/api/admin/categories/1")
                    .with(user(EMAIL).password(PASSWORD))
                    .with(csrf().asHeader())
                    .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk());
    }

    @Test
    public void testCreateASkill() throws Exception {
        Skill skill = new Skill();
        skill.setName("New Skill");
        skill.setCategoryId(1);

        given(adminSkillService.createByCategoryId(any(Skill.class), anyLong())).willReturn(skill);

        mvc.perform(post("/api/admin/categories/1/skills")
                    .with(user(EMAIL).password(PASSWORD))
                    .with(csrf().asHeader())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(skill)))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.name", is("New Skill")))
                    .andExpect(jsonPath("$.category_id", is(1)));
    }

    @Test
    public void testUpdateASkill() throws Exception {
        Skill skill = new Skill();
        skill.setName("Skill Updated");
        skill.setCategoryId(1);

        given(adminSkillService.updateByCategoryIdAndSkillId(any(Skill.class), anyLong(), anyLong())).willReturn(skill);

        mvc.perform(put("/api/admin/categories/1/skills/1")
                    .with(user(EMAIL).password(PASSWORD))
                    .with(csrf().asHeader())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(skill)))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.name", is("Skill Updated")));
    }

    @Test
    public void testDeleteASkill() throws Exception {
        mvc.perform(delete("/api/admin/categories/1/skills/1")
                    .with(user(EMAIL).password(PASSWORD))
                    .with(csrf().asHeader())
                    .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk());
    }
}
