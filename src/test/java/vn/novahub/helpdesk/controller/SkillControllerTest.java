package vn.novahub.helpdesk.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.collections.IteratorUtils;
import org.junit.Before;
import org.junit.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import vn.novahub.helpdesk.exception.CategoryNotFoundException;
import vn.novahub.helpdesk.exception.SkillNotFoundException;
import vn.novahub.helpdesk.model.Account;
import vn.novahub.helpdesk.model.AccountHasSkill;
import vn.novahub.helpdesk.model.Category;
import vn.novahub.helpdesk.model.Skill;
import vn.novahub.helpdesk.repository.AccountRepository;
import vn.novahub.helpdesk.service.AccountSkillService;
import vn.novahub.helpdesk.service.AdminSkillService;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.core.Is.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(SkillController.class)
public class SkillControllerTest extends BaseControllerTest {

    @MockBean
    private AccountSkillService accountSkillService;

    @MockBean
    private AdminSkillService adminSkillService;

    private ObjectMapper objectMapper;

    private List<Skill> skills;

    private List<Category> categories;

    private List<Account> accounts;

    private List<AccountHasSkill> accountHasSkills;

    @Before
    public void before() throws IOException {
        accounts = convertJsonFileToObjectList(
                "seeding/accounts.json",
                new TypeReference<List<Account>>(){});

        categories = convertJsonFileToObjectList(
                "seeding/categories.json",
                new TypeReference<List<Category>>(){});

        skills = convertJsonFileToObjectList(
                "seeding/skills.json",
                new TypeReference<List<Skill>>(){});

        accountHasSkills = convertJsonFileToObjectList(
                "seeding/accountHasSkills.json",
                new TypeReference<List<AccountHasSkill>>(){});

        objectMapper = new ObjectMapper();
    }

    @Test
    public void testGetAll() throws Exception {
        for (int i = skills.size() - 1; i >= 0; i--) {
            if(skills.get(i).getCategoryId() != 1) {
                skills.remove(i);
            }
        }

        given(accountSkillService.getAllByKeyword(1L, "", new PageRequest(0, 20))).willReturn(new PageImpl<>(skills));

        mvc.perform(get("/api/skills?categoryId=1")
                    .with(user(EMAIL).password(PASSWORD))
                    .with(csrf().asHeader())
                    .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.totalElements", is(skills.size())));
    }

    @Test
    public void testSearch() throws Exception {
        List<Long> skillIds = new ArrayList<>();
        skillIds.add(1L);
        skillIds.add(2L);

        given(accountSkillService.search(skillIds, new PageRequest(0, 20))).willReturn(new PageImpl<>(skills));

        mvc.perform(get("/api/skills/search?id=1,2")
                    .with(user(EMAIL).password(PASSWORD))
                    .with(csrf().asHeader())
                    .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.totalElements", is(skills.size())));

    }

    @Test
    public void testGetById() throws Exception {
        given(adminSkillService.findOne(anyLong())).willReturn(skills.get(0));

        mvc.perform(get("/api/skills/1")
                    .with(user(EMAIL).password(PASSWORD))
                    .with(csrf().asHeader())
                    .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.name", is(skills.get(0).getName())));
    }

    @Test
    public void testGetAllUsersBySkillId() throws Exception {
        List<Account> accountList = new ArrayList<>();

        for (int i = accountHasSkills.size() - 1; i >= 0; i--) {
            if(accountHasSkills.get(i).getSkillId() != 1) {
                accountHasSkills.remove(i);
            }
        }

        for(AccountHasSkill accountHasSkill : accountHasSkills) {
            for(int i = accounts.size() - 1; i >= 0; i--) {
                if(accountHasSkill.getAccountId() == accounts.get(i).getId()) {
                    accountList.add(accounts.get(i));
                    accounts.remove(i);
                    break;
                }
            }
        }

        given(accountSkillService.getAllUsersBySkillId(1L, new PageRequest(0, 20))).willReturn(new PageImpl<>(accountList));

        mvc.perform(get("/api/skills/1/users")
                    .with(user(EMAIL).password(PASSWORD))
                    .with(csrf().asHeader())
                    .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.totalElements", is(61)));
    }
}