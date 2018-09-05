package vn.novahub.helpdesk.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import vn.novahub.helpdesk.exception.CategoryNotFoundException;
import vn.novahub.helpdesk.exception.SkillIsExistException;
import vn.novahub.helpdesk.exception.SkillNotFoundException;
import vn.novahub.helpdesk.exception.SkillValidationException;
import vn.novahub.helpdesk.model.Skill;
import vn.novahub.helpdesk.service.AdminSkillService;

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

@WebMvcTest(SkillAdminController.class)
public class SkillAdminControllerTest extends BaseControllerTest {

    @MockBean
    private AdminSkillService adminSkillService;

    private List<Skill> skills;

    private ObjectMapper objectMapper;

    @Before
    public void before() throws IOException {
        skills = convertJsonFileToObjectList(
                "seeding/skills.json",
                new TypeReference<List<Skill>>(){});

        objectMapper = new ObjectMapper();
    }

    @Test
    public void testCreateByAdmin() throws Exception {
        Skill skill = skills.get(0);

        given(adminSkillService.create(any(Skill.class))).willReturn(skill);

        mvc.perform(post("/api/admin/skills")
                    .with(user(EMAIL).password(PASSWORD))
                    .with(csrf().asHeader())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(skill)))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.name", is(skills.get(0).getName())));
    }

    @Test
    public void testUpdateByAdmin() throws Exception {
        Skill skill = skills.get(0);
        skill.setName("ABC");

        given(adminSkillService.update(anyLong(), any(Skill.class))).willReturn(skill);

        mvc.perform(put("/api/admin/skills/1")
                    .with(user(EMAIL).password(PASSWORD))
                    .with(csrf().asHeader())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(skill)))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.name", is("ABC")));
    }

    @Test
    public void testDeleteByAdmin() throws Exception {
        mvc.perform(delete("/api/admin/skills/1")
                    .with(user(EMAIL).password(PASSWORD))
                    .with(csrf().asHeader())
                    .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk());
    }

}
