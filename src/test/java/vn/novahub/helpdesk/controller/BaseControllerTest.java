package vn.novahub.helpdesk.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.web.config.EnableSpringDataWebSupport;
import org.springframework.test.web.servlet.MockMvc;
import vn.novahub.helpdesk.BaseTest;
import vn.novahub.helpdesk.service.AccountService;

@EnableSpringDataWebSupport
public abstract class BaseControllerTest extends BaseTest {

    public static final String EMAIL = "helpdesk@novahub.vn";
    public static final String PASSWORD = "password";

    public static final String USER_EMAIL = "linhtran@novahub.vn";
    public static final String USER_PASSWORD = "password";

    @Autowired
    public MockMvc mvc;

    @MockBean
    public AccountService accountService;
}
