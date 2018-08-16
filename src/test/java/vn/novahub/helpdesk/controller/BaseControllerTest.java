package vn.novahub.helpdesk.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.web.config.EnableSpringDataWebSupport;
import org.springframework.test.web.servlet.MockMvc;
import vn.novahub.helpdesk.BaseTest;

@EnableSpringDataWebSupport
public abstract class BaseControllerTest extends BaseTest {

    public static final String EMAIL = "helpdesk@novahub.vn";
    public static final String PASSWORD = "password";

    @Autowired
    public MockMvc mvc;
}
