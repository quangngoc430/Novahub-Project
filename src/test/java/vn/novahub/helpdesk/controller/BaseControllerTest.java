package vn.novahub.helpdesk.controller;

import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

@ActiveProfiles("test")
@RunWith(SpringRunner.class)
//@WebMvcTest(RoleController.class)
public abstract class BaseControllerTest {

    @Autowired
    public MockMvc mvc;

    public static final String EMAIL = "helpdesk@novahub.vn";
    public static final String PASSWORD = "password";
}