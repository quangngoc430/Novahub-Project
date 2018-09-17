package vn.novahub.helpdesk.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import vn.novahub.helpdesk.exception.RoleNotFoundException;
import vn.novahub.helpdesk.model.DayOffType;
import vn.novahub.helpdesk.service.DayOffTypeService;

import java.io.IOException;
import java.util.List;

import static org.hamcrest.core.Is.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(DayOffTypeAdminController.class)
public class DayOffTypeAdminControllerTest extends BaseControllerTest {

    @MockBean
    private DayOffTypeService dayOffTypeService;

    private List<DayOffType> dayOffTypes;

    @Before
    public void before() throws IOException, RoleNotFoundException {
        dayOffTypes = convertJsonFileToObjectList(
                "seeding/day_off_type.json",
                new TypeReference<List<DayOffType>>(){});
        given(dayOffTypeService.getAllDayOffType())
                .willReturn(dayOffTypes);
    }

    @Test
    public void testCreate() throws Exception {
        given(dayOffTypeService.create(any(DayOffType.class))).willReturn(dayOffTypes.get(0));
        mvc.perform(post("/api/admin/day-off-types")
                .with(user(EMAIL).password(PASSWORD))
                .with(csrf().asHeader())
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(dayOffTypes.get(0))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.type", is(dayOffTypes.get(0).getType())));
    }

    @Test
    public void testUpdate() throws Exception {
        given(dayOffTypeService.update(any(DayOffType.class))).willReturn(dayOffTypes.get(0));

        mvc.perform(put("/api/admin/day-off-types/1")
                .with(user(EMAIL).password(PASSWORD))
                .with(csrf().asHeader())
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(dayOffTypes.get(0))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.type", is(dayOffTypes.get(0).getType())));
    }

    @Test
    public void testDelete() throws Exception {

        given(dayOffTypeService.delete(1)).willReturn(dayOffTypes.get(0));

        mvc.perform(delete("/api/admin/day-off-types/1")
                .with(user(EMAIL).password(PASSWORD))
                .with(csrf().asHeader())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.type", is(dayOffTypes.get(0).getType())));
    }
}