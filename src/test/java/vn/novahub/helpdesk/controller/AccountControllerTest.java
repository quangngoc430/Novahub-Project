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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
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
    public void testLogin() throws Exception {
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

    @Test
    public void testLoginWithGoogle() throws Exception {
        Token token = new Token();
        token.setAccessToken("0109199717071982");
        token.setAccountId(2);

        given(accountService.loginWithGoogle(any(Token.class))).willReturn(token);

        mvc.perform(post("/api/login/google")
                    .with(user(EMAIL).password(PASSWORD))
                    .with(csrf().asHeader())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(new Token("asdfgh123456"))))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.access_token", is("0109199717071982")))
                    .andExpect(jsonPath("$.account_id", is(2)));
    }

    @Test
    public void testLogout() throws Exception {
        mvc.perform(get("/api/logout")
                    .with(user(EMAIL).password(PASSWORD))
                    .with(csrf().asHeader())
                    .header("access_token", "124790asdfghjkl")
                    .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk());
    }

    @Test
    public void testGetAll() throws Exception {

        given(accountService.getAll("", "", "", PageRequest.of(0, 20))).willReturn(new PageImpl<>(accounts));

        mvc.perform(get("/api/users")
                    .with(user(EMAIL).password(PASSWORD))
                    .with(csrf().asHeader())
                    .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.numberOfElements", is(accounts.size())));
    }

    @Test
    public void testCreateAccount() throws Exception {
        Account account = new Account();
        account.setEmail(accounts.get(1).getEmail());
        account.setPassword("password");

        String json = objectMapper.writeValueAsString(account);

        given(accountService.create(any(Account.class))).willReturn(accounts.get(1));

        mvc.perform(post("/api/users")
                    .with(user(EMAIL).password(PASSWORD))
                    .with(csrf().asHeader())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(json))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.email", is(accounts.get(1).getEmail())));
    }

    @Test
    public void testGetAccountLogin() throws Exception {
        given(accountService.getAccountLogin()).willReturn(accounts.get(2));

        mvc.perform(get("/api/users/me")
                    .with(user(EMAIL).password(PASSWORD))
                    .with(csrf().asHeader())
                    .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.email", is(accounts.get(2).getEmail())));
    }

    @Test
    public void testUpdateForAccountLogin() throws Exception {
        Account account = accounts.get(1);
        account.setFirstName("Quang Ngoc");
        account.setLastName("Bui Lam");

        given(accountService.update(any(Account.class))).willReturn(account);

        mvc.perform(put("/api/users/me")
                    .with(user(EMAIL).password(PASSWORD))
                    .with(csrf().asHeader())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(account)))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.first_name", is("Quang Ngoc")));
    }

    @Test
    public void testGetById() throws Exception {
        given(accountService.get(1L)).willReturn(accounts.get(0));

        mvc.perform(get("/api/users/1")
                    .with(user(EMAIL).password(PASSWORD))
                    .with(csrf().asHeader())
                    .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.email", is(accounts.get(0).getEmail())));
    }
}
