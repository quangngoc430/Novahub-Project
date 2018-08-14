package vn.novahub.helpdesk.controller;

import org.junit.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.*;
import org.springframework.http.MediaType;
import vn.novahub.helpdesk.model.Role;
import vn.novahub.helpdesk.seeding.ApplicationSeeder;
import vn.novahub.helpdesk.service.RoleService;

import java.util.ArrayList;

import static org.hamcrest.core.Is.is;
import static org.mockito.BDDMockito.given;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(RoleController.class)
public class RoleControllerTest extends BaseControllerTest {

    @MockBean
    private ApplicationSeeder seeder;

    @MockBean
    private RoleService service;

    @Test
    public void testGetRoleById() throws Exception {
        final Role role = new Role();
        role.setId(1);
        role.setName("ADMIN");

        given(service.getById(1l)).willReturn(role);

        mvc.perform(get("/api/roles/1")
                .with(user(EMAIL).password(PASSWORD))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is("ADMIN")));
    }

    @Test
    public void testGetAllRoles() throws Exception {
        final ArrayList<Role> roleArrayList = new ArrayList<>();
        roleArrayList.add(new Role(1L, "ADMIN"));
        roleArrayList.add(new Role(2L, "CLERK"));
        roleArrayList.add(new Role(3L, "EMPLOYEE"));

        final Page<Role> rolePage = new PageImpl<>(roleArrayList);

        PageRequest pageRequest = new PageRequest(0, 20);

        given(service.getAll("", pageRequest)).willReturn(rolePage);

        mvc.perform(get("/api/roles")
                    .with(user(EMAIL).password(PASSWORD))
                    .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk());
    }

}
