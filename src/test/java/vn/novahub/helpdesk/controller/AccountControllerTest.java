package vn.novahub.helpdesk.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import vn.novahub.helpdesk.model.Account;
import vn.novahub.helpdesk.model.Token;
import vn.novahub.helpdesk.service.AccountService;
import vn.novahub.helpdesk.service.AccountSkillService;

import java.io.IOException;
import java.util.List;

import static org.hamcrest.core.Is.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AccountController.class)
public class AccountControllerTest extends BaseControllerTest {

    @MockBean
    private AccountService accountService;

    @MockBean
    private AccountSkillService accountSkillService;

    private ObjectMapper objectMapper;

    private List<Account> accounts;

    @Before
    public void before() throws IOException {
        accounts = convertJsonFileToObjectList(
                "seeding/accounts.json",
                new TypeReference<List<Account>>(){});
        objectMapper = new ObjectMapper();
    }

    @Test
    public void TestLogin() throws Exception {
        Token token = new Token();
        token.setAccessToken("123456789");
        token.setAccountId(1);
        given(accountService.login(any(Account.class))).willReturn(token);

        mvc.perform(post("/api/login")
                    .with(user(EMAIL).password(PASSWORD))
                    .with(csrf().asHeader())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(accounts.get(0))))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.access_token", is("123456789")))
                    .andExpect(jsonPath("$.account_id", is(1)));
    }

}
