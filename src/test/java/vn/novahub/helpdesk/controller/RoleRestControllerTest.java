package vn.novahub.helpdesk.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.data.domain.Page;
import org.springframework.http.*;
import org.springframework.test.context.junit4.SpringRunner;
import vn.novahub.helpdesk.enums.RoleEnum;
import vn.novahub.helpdesk.exception.AccountInactiveException;
import vn.novahub.helpdesk.exception.AccountInvalidException;
import vn.novahub.helpdesk.exception.AccountLockedException;
import vn.novahub.helpdesk.exception.AccountValidationException;
import vn.novahub.helpdesk.model.Account;
import vn.novahub.helpdesk.model.Role;
import vn.novahub.helpdesk.model.Token;
import vn.novahub.helpdesk.service.AccountService;
import vn.novahub.helpdesk.service.impl.AccountServiceImpl;

import java.io.IOException;

import static junit.framework.TestCase.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class RoleRestControllerTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    private Token token;
    private String url;

    @InjectMocks
    private AccountServiceImpl accountServiceImpl;

    @Before
    public void before() throws IOException {
        url = "http://localhost:" + port;

        ObjectMapper objectMapper = new ObjectMapper();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        String email = "helpdesk@novahub.vn";
        String password = "password";
        String requestBodyJson = "{\"email\":\"" + email + "\", \"password\":\"" + password + "\"}";

        HttpEntity<String> entity = new HttpEntity<>(requestBodyJson, headers);
        ResponseEntity<?> responseEntity = restTemplate.exchange(url + "/api/login", HttpMethod.POST, entity, Object.class);

        token = objectMapper.convertValue(responseEntity.getBody(), Token.class);
    }

    @Test
    public void testGetARole() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("access_token", token.getAccessToken());

        ObjectMapper objectMapper = new ObjectMapper();
        HttpEntity entity = new HttpEntity(headers);

        ResponseEntity<?> responseEntity = restTemplate.exchange(url + "/api/roles/1", HttpMethod.GET, entity, Object.class);
        Role role = objectMapper.convertValue(responseEntity.getBody(), Role.class);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(RoleEnum.ADMIN.name(), role.getName());

        responseEntity = restTemplate.exchange(url + "/api/roles/4", HttpMethod.GET, entity, Object.class);
        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
    }

    @Test
    public void testGetAllRoles() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("access_token", token.getAccessToken());

        ObjectMapper objectMapper = new ObjectMapper();
        HttpEntity entity = new HttpEntity(headers);

        ResponseEntity<?> responseEntity = restTemplate.exchange(url + "/api/roles", HttpMethod.GET, entity, Object.class);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
    }

    @After
    public void after() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("access_token", token.getAccessToken());

        HttpEntity entity = new HttpEntity(headers);

        restTemplate.exchange(url + "/api/logout", HttpMethod.GET, entity, Object.class);
    }

}