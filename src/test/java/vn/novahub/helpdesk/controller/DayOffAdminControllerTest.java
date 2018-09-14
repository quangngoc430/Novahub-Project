package vn.novahub.helpdesk.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import org.apache.http.impl.execchain.RequestAbortedException;
import org.junit.Before;
import org.junit.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import vn.novahub.helpdesk.enums.DayOffStatus;
import vn.novahub.helpdesk.exception.RoleNotFoundException;
import vn.novahub.helpdesk.exception.dayoff.DayOffNotFoundException;
import vn.novahub.helpdesk.exception.dayofftype.DayOffTypeNotFoundException;
import vn.novahub.helpdesk.model.Account;
import vn.novahub.helpdesk.model.DayOff;
import vn.novahub.helpdesk.service.AccountService;
import vn.novahub.helpdesk.service.DayOffService;

import java.io.IOException;
import java.util.List;

import static org.hamcrest.core.Is.is;
import static org.mockito.BDDMockito.given;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(DayOffAdminController.class)
public class DayOffAdminControllerTest extends BaseControllerTest {

    @MockBean
    private DayOffService dayOffService;

    private List<Account> accounts;

    private List<DayOff> dayOffs;

    @Before
    public void before() throws IOException, RoleNotFoundException {
        accounts = convertJsonFileToObjectList(
                "seeding/accounts.json",
                new TypeReference<List<Account>>(){});
        dayOffs = convertJsonFileToObjectList(
                "seeding/day_off.json",
                new TypeReference<List<DayOff>>(){});
        given(dayOffService.getAllByAccountIdAndStatus(1, "", PageRequest.of(0, 20)))
                .willReturn(new PageImpl<>(dayOffs));
    }

    @Test
    public void testAnswerWithToken_approve() throws Exception {
        String status = DayOffStatus.APPROVED.name();
        dayOffs.get(0).setStatus(status);
        given(dayOffService.approve(1L, "12345")).willReturn(dayOffs.get(0));

        mvc.perform(get("/api/admin/day-offs/1/answer-token")
                .with(user(EMAIL).password(PASSWORD))
                .param("status", status)
                .param("token", "12345")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status", is(status)));
    }

    @Test
    public void testAnswerWithToken_denied() throws Exception {
        String status = DayOffStatus.DENIED.name();
        dayOffs.get(0).setStatus(status);
        given(dayOffService.deny(1L, "12345")).willReturn(dayOffs.get(0));

        mvc.perform(get("/api/admin/day-offs/1/answer-token")
                .with(user(EMAIL).password(PASSWORD))
                .param("status", status)
                .param("token", "12345")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status", is(status)));
    }

    @Test
    public void testAnswerWithToken_exception() throws Exception {
        String status1 = "wrong status";
        String status2 = DayOffStatus.PENDING.name();
        mvc.perform(get("/api/admin/day-offs/1/answer-token")
                .with(user(EMAIL).password(PASSWORD))
                .param("status", status1)
                .param("token", "12345")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());

        mvc.perform(get("/api/admin/day-offs/1/answer-token")
                .with(user(EMAIL).password(PASSWORD))
                .param("status", status2)
                .param("token", "12345")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testAnswer_approve() throws Exception {
        String status = DayOffStatus.APPROVED.name();
        dayOffs.get(0).setStatus(status);
        given(dayOffService.approve(1L)).willReturn(dayOffs.get(0));

        mvc.perform(get("/api/admin/day-offs/1/answer")
                .with(user(EMAIL).password(PASSWORD))
                .param("status", status)
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status", is(status)));
    }

    @Test
    public void testAnswer_denied() throws Exception {
        String status = DayOffStatus.DENIED.name();
        dayOffs.get(0).setStatus(status);
        given(dayOffService.deny(1L)).willReturn(dayOffs.get(0));

        mvc.perform(get("/api/admin/day-offs/1/answer")
                .with(user(EMAIL).password(PASSWORD))
                .param("status", status)
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status", is(status)));
    }

    @Test
    public void testAnswer_cancelled() throws Exception {
        String status = DayOffStatus.CANCELLED.name();
        dayOffs.get(0).setStatus(status);
        given(dayOffService.cancel(1L)).willReturn(dayOffs.get(0));

        mvc.perform(get("/api/admin/day-offs/1/answer")
                .with(user(EMAIL).password(PASSWORD))
                .param("status", status)
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status", is(status)));
    }

    @Test
    public void testAnswer_exception() throws Exception {
        String status1 = "wrong status";
        String status2 = DayOffStatus.PENDING.name();
        mvc.perform(get("/api/admin/day-offs/1/answer")
                .with(user(EMAIL).password(PASSWORD))
                .param("status", status1)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());

        mvc.perform(get("/api/admin/day-offs/1/answer")
                .with(user(EMAIL).password(PASSWORD))
                .param("status", status2)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testGetAll() throws Exception {

        given(dayOffService.getAllByStatus("", PageRequest.of(0, 20)))
                            .willReturn(new PageImpl<>(dayOffs));

        mvc.perform(get("/api/admin/day-offs")
                .with(user(EMAIL).password(PASSWORD))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.length()", is(3)));
    }

    @Test
    public void testGetAllByAccountId() throws Exception {
        dayOffs.remove(2);
        given(dayOffService.getAllByAccountIdAndStatus(1,"", PageRequest.of(0, 20)))
                .willReturn(new PageImpl<>(dayOffs));

        mvc.perform(get("/api/admin/day-offs?accountId=1")
                .with(user(EMAIL).password(PASSWORD))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.length()", is(2)));
    }
}