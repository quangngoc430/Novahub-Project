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
import vn.novahub.helpdesk.model.DayOff;
import vn.novahub.helpdesk.model.DayOffAccount;
import vn.novahub.helpdesk.model.DayOffType;
import vn.novahub.helpdesk.service.DayOffService;

import java.io.IOException;
import java.util.List;

import static org.hamcrest.core.Is.is;
import static org.mockito.BDDMockito.given;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(DayOffController.class)
public class DayOffControllerTest extends BaseControllerTest {

    @MockBean
    private DayOffService dayOffService;

    private List<Account> accounts;

    private List<DayOffType> dayOffTypes;

    private List<DayOffAccount> dayOffAccounts;

    private List<DayOff> dayOffs;

    @Before
    public void before() throws IOException, RoleNotFoundException {
        accounts = convertJsonFileToObjectList(
                "seeding/accounts.json",
                new TypeReference<List<Account>>(){});
        dayOffTypes = convertJsonFileToObjectList(
                "seeding/day_off_type.json",
                new TypeReference<List<Account>>(){});
        dayOffAccounts = convertJsonFileToObjectList(
                "seeding/day_off_account.json",
                new TypeReference<List<Account>>(){});
        dayOffs = convertJsonFileToObjectList(
                "seeding/day_off.json",
                new TypeReference<List<Account>>(){});
    }

    @Test
    public void testUserLoginDayOffs() throws Exception {
        given(dayOffService.getAllByAccountIdAndStatus(1, "", PageRequest.of(0, 20)))
                .willReturn(new PageImpl<>(dayOffs));

        mvc.perform(get("/api/day-offs")
                .with(user(EMAIL).password(PASSWORD))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.length()", is(3)));
    }

//    @Test
//    public void testGetAllRoles() throws Exception {
//        mvc.perform(get("/api/roles?page=0&size=20")
//                .with(user(EMAIL).password(PASSWORD))
//                .contentType(MediaType.APPLICATION_JSON))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.content.length()", is(3)))
//                .andExpect(jsonPath("$.content[0].name", is("ADMIN")));
//    }

}