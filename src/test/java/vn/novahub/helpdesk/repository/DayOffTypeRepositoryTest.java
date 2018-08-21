package vn.novahub.helpdesk.repository;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import vn.novahub.helpdesk.model.DayOffType;

import static org.junit.Assert.*;

public class DayOffTypeRepositoryTest extends BaseRepositoryTest{

    @Autowired
    private DayOffTypeRepository dayOffTypeRepository;

    private DayOffType dayOffType;

    @Before
    public void before() {
        dayOffTypeRepository.deleteAll();
        dayOffType = new DayOffType();
        dayOffType.setType("Pregnant");
        dayOffType.setDefaultQuota(24);
    }

    @After
    public void after() {
        dayOffTypeRepository.deleteAll();
    }

    @Test
    public void testSaveAndFindByType() throws Exception {
        dayOffTypeRepository.save(dayOffType);
        assertEquals(dayOffType, dayOffTypeRepository.findByType("Pregnant"));
    }

}