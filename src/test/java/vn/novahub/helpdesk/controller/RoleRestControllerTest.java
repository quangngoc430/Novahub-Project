package vn.novahub.helpdesk.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.*;
import org.springframework.test.context.junit4.SpringRunner;
import vn.novahub.helpdesk.model.Token;

import java.io.IOException;

import static junit.framework.TestCase.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class RoleRestControllerTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    private static Token token;

    @Before
    public void setup() throws IOException {
        if(token == null) {

            ObjectMapper objectMapper = new ObjectMapper();

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            String email = "helpdesk@novahub.vn";
            String password = "password";
            String requestBodyJson = "{\"email\":\"" + email + "\", \"password\":\"" + password + "\"}";


            HttpEntity<String> entity = new HttpEntity<>(requestBodyJson, headers);
            ResponseEntity<?> responseEntity = restTemplate.exchange("http://localhost:" + port + "/api/login", HttpMethod.POST, entity, Object.class);


            token = objectMapper.convertValue(responseEntity.getBody(), Token.class);
            System.out.println(token);
        }
    }

    @Test
    public void getARole_shouldReturnNotFound() throws Exception {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("access_token", token.getAccessToken());

        HttpEntity entity = new HttpEntity(headers);


        ResponseEntity<?> responseEntity = restTemplate.exchange("http://localhost:" + port + "/api/roles/4", HttpMethod.GET, entity, Object.class);
        System.out.println(responseEntity.getBody());
        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
    }

    @Test
    public void a() {

    }

}
