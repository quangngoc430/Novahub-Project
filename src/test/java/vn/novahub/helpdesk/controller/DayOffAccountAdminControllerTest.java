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
import vn.novahub.helpdesk.exception.RoleNotFoundException;
import vn.novahub.helpdesk.model.Account;
import vn.novahub.helpdesk.model.DayOffAccount;
import vn.novahub.helpdesk.service.AccountService;
import vn.novahub.helpdesk.service.DayOffAccountService;

import java.io.IOException;
import java.util.List;

import static org.hamcrest.core.Is.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(DayOffAccountAdminController.class)
public class DayOffAccountAdminControllerTest extends BaseControllerTest {

    @MockBean
    private DayOffAccountService dayOffAccountService;

    @MockBean
    private AccountService accountService;

    private ObjectMapper objectMapper;

    private List<DayOffAccount> dayOffAccounts;

    private List<Account> accounts;

    @Before
    public void before() throws IOException, RoleNotFoundException {
        dayOffAccounts = convertJsonFileToObjectList(
                "seeding/day_off_account.json",
                new TypeReference<List<DayOffAccount>>(){});
        accounts = convertJsonFileToObjectList(
                "seeding/accounts.json",
                new TypeReference<List<Account>>(){});
        given(accountService.getAccountLogin()).willReturn(accounts.get(0));
        objectMapper = new ObjectMapper();
    }

    @Test
    public void testCreate() throws Exception {
        given(dayOffAccountService.add(any(DayOffAccount.class))).willReturn(dayOffAccounts.get(0));

        mvc.perform(post("/api/admin/day-off-accounts")
                .with(user(EMAIL).password(PASSWORD))
                .with(csrf().asHeader())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dayOffAccounts.get(0))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.privateQuota", is(dayOffAccounts.get(0).getPrivateQuota())));
    }

    @Test
    public void testGet_noParam() throws Exception {
        given(dayOffAccountService.getAll(PageRequest.of(0, 20)))
                .willReturn(new PageImpl<>(dayOffAccounts));

        mvc.perform(get("/api/admin/day-off-accounts")
                .with(user(EMAIL).password(PASSWORD))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.length()", is(dayOffAccounts.size())))
                .andExpect(jsonPath("$.content[1].remainingTime", is(dayOffAccounts.get(1).getRemainingTime())));
    }

    @Test
    public void testGet_withParameterOfAccountId() throws Exception {

        dayOffAccounts.remove(2);

        given(dayOffAccountService.findByAccountId(1, PageRequest.of(0, 20)))
                .willReturn(new PageImpl<>(dayOffAccounts));

        mvc.perform(get("/api/admin/day-off-accounts")
                .with(user(EMAIL).password(PASSWORD))
                .param("accountId", "1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.length()", is(dayOffAccounts.size())));
    }

    @Test
    public void testUpdate() throws Exception {

        given(dayOffAccountService.update(any(DayOffAccount.class))).willReturn(dayOffAccounts.get(0));

        mvc.perform(put("/api/admin/day-off-accounts/1")
                .with(user(EMAIL).password(PASSWORD))
                .with(csrf().asHeader())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dayOffAccounts.get(0))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.privateQuota", is(dayOffAccounts.get(0).getPrivateQuota())));
    }




}