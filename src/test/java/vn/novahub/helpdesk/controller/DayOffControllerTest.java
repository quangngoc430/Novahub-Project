package vn.novahub.helpdesk.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import vn.novahub.helpdesk.exception.AccountNotFoundException;
import vn.novahub.helpdesk.exception.DayOffIsNotExistException;
import vn.novahub.helpdesk.exception.RoleNotFoundException;
import vn.novahub.helpdesk.model.Account;
import vn.novahub.helpdesk.model.DayOff;
import vn.novahub.helpdesk.model.DayOffAccount;
import vn.novahub.helpdesk.model.DayOffType;
import vn.novahub.helpdesk.service.AccountService;
import vn.novahub.helpdesk.service.DayOffService;

import java.io.IOException;
import java.util.List;

import static org.hamcrest.core.Is.is;
import static org.mockito.BDDMockito.given;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(DayOffController.class)
public class DayOffControllerTest extends BaseControllerTest {

    @MockBean
    private DayOffService dayOffService;

    @MockBean
    private AccountService accountService;

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
                new TypeReference<List<DayOffType>>(){});
        dayOffAccounts = convertJsonFileToObjectList(
                "seeding/day_off_account.json",
                new TypeReference<List<DayOffAccount>>(){});
        dayOffs = convertJsonFileToObjectList(
                "seeding/day_off.json",
                new TypeReference<List<DayOff>>(){});

        given(dayOffService.getAllByAccountIdAndStatus(1, "", PageRequest.of(0, 20)))
                .willReturn(new PageImpl<>(dayOffs));

    }

    @Test
    public void testGetUserLoginDayOffs() throws Exception {
        Account adminAccount = accounts.get(0);
        given(accountService.getAccountLogin()).willReturn(adminAccount);

        mvc.perform(get("/api/day-offs")
                .with(user(EMAIL).password(PASSWORD))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.length()", is(3)))
                .andExpect(jsonPath("$.content[0].status", is("APPROVED")));
    }

    @Test
    public void testGetUserLoginDayOffs_noResult() throws Exception {
        Account userAccount = accounts.get(2);
        this.dayOffs.clear();
        given(dayOffService.getAllByAccountIdAndStatus(3, "", PageRequest.of(0, 20)))
                .willReturn(new PageImpl<>(dayOffs));
        given(accountService.getAccountLogin()).willReturn(userAccount);

        mvc.perform(get("/api/day-offs")
                .with(user(USER_EMAIL).password(USER_PASSWORD))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.length()", is(0)));
    }

    @Test
    public void testGetById() throws Exception {
        Account userAccount = accounts.get(1);
        given(accountService.getAccountLogin()).willReturn(userAccount);
        given(dayOffService.getById(2L)).willReturn(dayOffs.get(1));

        mvc.perform(get("/api/day-offs/2")
                .with(user(USER_EMAIL).password(USER_PASSWORD))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(2)))
                .andExpect(jsonPath("$.status", is("PENDING")));
    }

    @Test
    public void testGetById_dayOffIsNotExist() throws Exception {
        Account adminAccount = accounts.get(0);
        given(accountService.getAccountLogin()).willReturn(adminAccount);
        given(dayOffService.getById(5L)).willThrow(new DayOffIsNotExistException(5L));

        mvc.perform(get("/api/day-offs/5")
                .with(user(EMAIL).password(PASSWORD))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testGetById_accountNotFoundException() throws Exception {
        Account userAccount = accounts.get(1);
        given(accountService.getAccountLogin()).willReturn(userAccount);
        given(dayOffService.getById(1L)).willThrow(new AccountNotFoundException(1L));

        mvc.perform(get("/api/day-offs/1")
                .with(user(USER_EMAIL).password(USER_PASSWORD))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testCreate() throws Exception {
        Logger logger = LoggerFactory.getLogger(this.getClass());
//        Account adminAccount = accounts.get(0);
//        given(accountService.getAccountLogin()).willReturn(adminAccount);

        DayOff newDayOff = mockDayOff(1);
        String json = new ObjectMapper().writeValueAsString(newDayOff);


        given(dayOffService.add(newDayOff)).willReturn(dayOffs.get(1));
        logger.info(json);

        mvc.perform(post("/api/day-offs")
                .with(user(EMAIL).password(PASSWORD).roles("USER", "ADMIN"))
                .with(csrf().asHeader())
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(json))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status", is("PENDING")));
    }

    private DayOff mockDayOff(int index) {
        DayOff newDayOff = new DayOff();
        newDayOff.setNumberOfHours(dayOffs.get(index).getNumberOfHours());
        newDayOff.setStartDate(dayOffs.get(index).getStartDate());
        newDayOff.setEndDate(dayOffs.get(index).getEndDate());
        newDayOff.setComment(dayOffs.get(index).getComment());
        return newDayOff;
    }

    private DayOffAccount mockDayOffAccount(int index) {
        DayOffAccount dayOffAccount= new DayOffAccount();
        dayOffAccount.setDayOffTypeId(dayOffAccounts.get(index).getDayOffTypeId());
        dayOffAccount.setYear(dayOffAccounts.get(index).getYear());
        return dayOffAccount;
    }

    private String createJsonPost(DayOff newDayOff, DayOffAccount dayOffAccount) throws JsonProcessingException {

        String jsonDayOff = new ObjectMapper().writeValueAsString(newDayOff);
        jsonDayOff = jsonDayOff.replace("}", ",");

        String jsonDayOffAccount = new ObjectMapper().writeValueAsString(dayOffAccount);


        return jsonDayOff + "\"dayOffAccount\": " + jsonDayOffAccount + "}";

    }



}