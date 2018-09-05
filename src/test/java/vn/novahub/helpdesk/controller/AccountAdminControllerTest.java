package vn.novahub.helpdesk.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import vn.novahub.helpdesk.model.Account;
import vn.novahub.helpdesk.service.AccountService;

import java.io.IOException;
import java.util.List;

import static org.hamcrest.core.Is.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AccountAdminController.class)
public class AccountAdminControllerTest extends BaseControllerTest {

    @MockBean
    private AccountService accountService;

    private List<Account> accounts;

    private ObjectMapper objectMapper;

    @Before
    public void before() throws IOException {
        accounts = convertJsonFileToObjectList(
                "seeding/accounts.json",
                new TypeReference<List<Account>>(){});

        objectMapper = new ObjectMapper();
    }

    @Test
    public void testUpdate() throws Exception {
        Account accountUpdated = accounts.get(2);
        accountUpdated.setTitle("Front-end engineer");

        given(accountService.updatedForAdmin(anyLong(), any(Account.class))).willReturn(accountUpdated);

        mvc.perform(put("/api/admin/users/3")
                    .with(user(EMAIL).password(PASSWORD))
                    .with(csrf().asHeader())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(accountUpdated)))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.title", is("Front-end engineer")));
    }

    @Test
    public void testDelete() throws Exception {
        mvc.perform(delete("/api/admin/users/2")
                    .with(user(EMAIL).password(PASSWORD))
                    .with(csrf().asHeader())
                    .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk());
    }
}