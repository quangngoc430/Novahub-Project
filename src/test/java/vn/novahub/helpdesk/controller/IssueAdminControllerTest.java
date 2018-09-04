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
import vn.novahub.helpdesk.enums.IssueEnum;
import vn.novahub.helpdesk.model.Issue;
import vn.novahub.helpdesk.service.AdminIssueService;

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

@WebMvcTest(IssueAdminController.class)
public class IssueAdminControllerTest extends BaseControllerTest {

    @MockBean
    private AdminIssueService adminIssueService;

    private ObjectMapper objectMapper;

    private List<Issue> issues;

    @Before
    public void before() throws IOException {
        issues = convertJsonFileToObjectList(
                "seeding/issues.json",
                new TypeReference<List<Issue>>(){});

        objectMapper = new ObjectMapper();
    }

    @Test
    public void testGetAllByAdmin() throws Exception {
        for (int i = issues.size() - 1; i >= 0; i--) {
            if (!issues.get(i).getStatus().equals(IssueEnum.PENDING.name()))
                issues.remove(i);
        }

        given(adminIssueService.getAllByKeywordAndStatus("", "PENDING", new PageRequest(0, 20))).willReturn(new PageImpl<>(issues));

        mvc.perform(get("/api/admin/issues")
                    .with(user(EMAIL).password(PASSWORD))
                    .with(csrf().asHeader())
                    .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk());
    }

    @Test
    public void testFindOneByAdmin() throws Exception {
        given(adminIssueService.findOne(1)).willReturn(issues.get(0));

        mvc.perform(get("/api/admin/issues/1")
                    .with(user(EMAIL).password(PASSWORD))
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.title", is(issues.get(0).getTitle())));
    }

    @Test
    public void testUpdateForAmin() throws Exception {
        Issue issue = issues.get(0);
        issue.setTitle("1234");

        given(adminIssueService.update(anyLong(), any(Issue.class))).willReturn(issue);

        mvc.perform(put("/api/admin/issues/1")
                    .with(user(EMAIL).password(PASSWORD))
                    .with(csrf().asHeader())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(issue)))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.title", is("1234")));
    }

    @Test
    public void testDeleteByAdmin() throws Exception {
        mvc.perform(delete("/api/admin/issues/1")
                    .with(user(EMAIL).password(PASSWORD))
                    .with(csrf().asHeader())
                    .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk());
    }

    @Test
    public void testActionForAdmin() throws Exception {
        mvc.perform(put("/api/admin/issues/1/action?status=APPROVE")
                    .with(user(EMAIL).password(PASSWORD))
                    .with(csrf().asHeader())
                    .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk());
    }
}
