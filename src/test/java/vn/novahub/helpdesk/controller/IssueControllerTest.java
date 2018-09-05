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
import vn.novahub.helpdesk.model.Account;
import vn.novahub.helpdesk.model.Issue;
import vn.novahub.helpdesk.service.AccountIssueService;

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

@WebMvcTest(IssueController.class)
public class IssueControllerTest extends BaseControllerTest {

    @MockBean
    private AccountIssueService accountIssueService;

    private ObjectMapper objectMapper;

    private List<Account> accounts;

    private List<Issue> issues;

    @Before
    public void before() throws IOException {
        accounts = convertJsonFileToObjectList(
                "seeding/accounts.json",
                new TypeReference<List<Account>>(){});

        issues = convertJsonFileToObjectList(
                "seeding/issues.json",
                new TypeReference<List<Issue>>(){});

        objectMapper = new ObjectMapper();
    }

    @Test
    public void testGetAll() throws Exception {
        for (int i = issues.size() - 1; i >= 0; i--) {
            if (issues.get(i).getAccountId() != 1)
                issues.remove(i);
        }

        given(accountIssueService.getAllByKeywordAndStatus("", "", new PageRequest(0, 20))).willReturn(new PageImpl<>(issues));

        mvc.perform(get("/api/issues/me")
                    .with(user(EMAIL).password(PASSWORD))
                    .with(csrf().asHeader())
                    .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.totalElements", is(issues.size())));
    }

    @Test
    public void testCreate() throws Exception {
        Issue issue = issues.get(0);

        given(accountIssueService.create(any(Issue.class))).willReturn(issue);

        mvc.perform(post("/api/issues/me")
                    .with(user(EMAIL).password(PASSWORD))
                    .with(csrf().asHeader())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(issue)))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.title", is(issues.get(0).getTitle())));
    }

    @Test
    public void testFindOne() throws Exception {
        given(accountIssueService.findOne(1L)).willReturn(issues.get(0));

        mvc.perform(get("/api/issues/me/1")
                    .with(user(EMAIL).password(PASSWORD))
                    .with(csrf().asHeader())
                    .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.title", is(issues.get(0).getTitle())));
    }

    @Test
    public void testUpdate() throws Exception {
        Issue issue = issues.get(0);
        issue.setTitle("ABC");

        given(accountIssueService.update(anyLong(), any(Issue.class))).willReturn(issue);

        mvc.perform(put("/api/issues/me/1")
                    .with(user(EMAIL).password(PASSWORD))
                    .with(csrf().asHeader())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(issue)))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.title", is("ABC")));
    }

    @Test
    public void testDelete() throws Exception {

        mvc.perform(delete("/api/issues/me/1")
                    .with(user(EMAIL).password(PASSWORD))
                    .with(csrf().asHeader())
                    .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk());
    }

    @Test
    public void testAction() throws Exception {
        mvc.perform(get("/api/issues/1/action?status=APPROVE&token=123456789")
                    .with(user(EMAIL).password(PASSWORD))
                    .with(csrf().asHeader())
                    .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk());
    }
}
