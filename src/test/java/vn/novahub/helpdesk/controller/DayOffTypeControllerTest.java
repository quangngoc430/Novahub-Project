package vn.novahub.helpdesk.controller;

import com.fasterxml.jackson.core.type.TypeReference;
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
import static org.mockito.BDDMockito.given;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(DayOffTypeController.class)
public class DayOffTypeControllerTest extends BaseControllerTest {

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
    public void testGetAllType() throws Exception {
        mvc.perform(get("/api/day-off-types")
                .with(user(EMAIL).password(PASSWORD))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()", is(dayOffTypes.size())));
    }

    @Test
    public void testTypeById() throws Exception {
        given(dayOffTypeService.getById(1)).willReturn(dayOffTypes.get(0));
        mvc.perform(get("/api/day-off-types/1")
                .with(user(EMAIL).password(PASSWORD))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.type", is(dayOffTypes.get(0).getType())));
    }

}