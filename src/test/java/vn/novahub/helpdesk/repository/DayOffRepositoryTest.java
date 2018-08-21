package vn.novahub.helpdesk.repository;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import vn.novahub.helpdesk.model.DayOff;

import java.util.List;

import static org.junit.Assert.*;

public class DayOffRepositoryTest {

    @Autowired
    private DayOffRepository repository;

    private Page<DayOff> dayOffPage;

    @Before
    public void before() {
        repository.deleteAll();

    }

    @After
    public void after() {
        repository.deleteAll();
    }

    @Test
    public void findNonCancelledByAccountId() throws Exception {
    }

    @Test
    public void findByKeyword() throws Exception {
    }

    @Test
    public void findNonCancelledByKeyword() throws Exception {
    }

    private List<DayOff> generateDayOff() {
        return null;
    }

}