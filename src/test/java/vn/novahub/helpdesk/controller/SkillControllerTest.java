package vn.novahub.helpdesk.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
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
import vn.novahub.helpdesk.exception.*;
import vn.novahub.helpdesk.model.Account;
import vn.novahub.helpdesk.model.AccountHasSkill;
import vn.novahub.helpdesk.model.Category;
import vn.novahub.helpdesk.model.Skill;
import vn.novahub.helpdesk.service.AccountSkillService;
import vn.novahub.helpdesk.service.AdminSkillService;

import java.io.IOException;
import java.util.ArrayList;
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
        given(accountSkillService.getAllByKeyword( "", new PageRequest(0, 20))).willReturn(new PageImpl<>(skills));

        mvc.perform(get("/api/skills?keyword=")
                    .with(user(EMAIL).password(PASSWORD))
                    .with(csrf().asHeader())
                    .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.totalElements", is(skills.size())));
    }

//    @Test
//    public void testSearch() throws Exception {
//        List<Long> skillIds = new ArrayList<>();
//        skillIds.add(1L);
//        skillIds.add(2L);
//
//        given(accountSkillService.search(skillIds, new PageRequest(0, 20))).willReturn(new PageImpl<>(skills));
//
//        mvc.perform(get("/api/skills/search?id=1,2")
//                    .with(user(EMAIL).password(PASSWORD))
//                    .with(csrf().asHeader())
//                    .contentType(MediaType.APPLICATION_JSON))
//                    .andExpect(status().isOk())
//                    .andExpect(jsonPath("$.totalElements", is(skills.size())));
//
//    }

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
                    .andExpect(jsonPath( "$.totalElements", is(61)));
    }

    @Test
    public void getAllByAccountLogin() throws Exception {
        for (int i = accountHasSkills.size() - 1; i >= 0; i--) {
            if (accountHasSkills.get(i).getAccountId() != 1) {
                accountHasSkills.remove(i);
            }
        }

        boolean check;

        for (int i = skills.size() - 1; i >= 0; i--) {
            check = false;
            for (int j = accountHasSkills.size() - 1; j >= 0; j--) {
                if(skills.get(i).getId() == accountHasSkills.get(j).getSkillId()) {
                    skills.get(i).setLevel(accountHasSkills.get(j).getLevel());
                    check = true;
                    break;
                }
            }

            if(!check) skills.remove(i);
        }

        given(accountSkillService.getAllByKeywordForAccountLogin("", new PageRequest(0, 20))).willReturn(new PageImpl<>(skills));

        mvc.perform(get("/api/skills/me")
                    .with(user(EMAIL).password(PASSWORD))
                    .with(csrf().asHeader())
                    .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.content[0].name", is("Android Development")))
                    .andExpect(jsonPath("$.totalElements", is(skills.size())));
    }

    @Test
    public void testCreate() throws Exception {
        Skill skill = skills.get(5);
        skill.setLevel(7);

        given(accountSkillService.create(any(Skill.class))).willReturn(skill);

        mvc.perform(post("/api/skills/me")
                    .with(user(EMAIL).password(PASSWORD))
                    .with(csrf().asHeader())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(skill)))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.name", is(skills.get(5).getName())))
                    .andExpect(jsonPath("$.level", is(7)));
    }

    @Test
    public void testGet() throws Exception {
        Skill skill = skills.get(1);
        skill.setLevel(9);

        given(accountSkillService.findOne(2L)).willReturn(skill);

        mvc.perform(get("/api/skills/me/2")
                    .with(user(EMAIL).password(PASSWORD))
                    .with(csrf().asHeader())
                    .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.name", is(skills.get(1).getName())))
                    .andExpect(jsonPath("$.level", is(9)));
    }

    @Test
    public void testUpdate() throws Exception {
        Skill skill = skills.get(4);
        skill.setName("IOS development");
        skill.setLevel(3);

        given(accountSkillService.update(anyLong(), any(Skill.class))).willReturn(skill);

        mvc.perform(put("/api/skills/me/5")
                    .with(user(EMAIL).password(PASSWORD))
                    .with(csrf().asHeader())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(skill)))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.name", is("IOS development")))
                    .andExpect(jsonPath("$.level", is(3)));
    }

    @Test
    public void testDelete() throws Exception {
        mvc.perform(delete("/api/skills/me/5")
                    .with(user(EMAIL).password(PASSWORD))
                    .with(csrf().asHeader())
                    .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk());
    }
}