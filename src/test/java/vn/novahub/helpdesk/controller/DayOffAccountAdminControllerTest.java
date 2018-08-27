package vn.novahub.helpdesk.controller;

import com.fasterxml.jackson.core.type.TypeReference;
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
import static org.mockito.BDDMockito.given;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(DayOffAccountController.class)
public class DayOffAccountAdminControllerTest extends BaseControllerTest {

    @MockBean
    private DayOffAccountService dayOffAccountService;

    @MockBean
    private AccountService accountService;

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

    }

    @Test
    public void testCreate() throws Exception {

        dayOffAccounts.remove(2);
        given(dayOffAccountService.findByAccountId(1, PageRequest.of(0, 20)))
                .willReturn(new PageImpl<>(dayOffAccounts));

        mvc.perform(get("/api/day-off-accounts")
                .with(user(EMAIL).password(PASSWORD))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.length()", is(2)))
                .andExpect(jsonPath("$.content[0].privateQuota", is(88)));
    }


}