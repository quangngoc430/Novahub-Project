package vn.novahub.helpdesk.repository;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import vn.novahub.helpdesk.model.*;

import java.io.IOException;
import java.util.List;

import static org.junit.Assert.*;

public class DayOffRepositoryTest extends BaseRepositoryTest{

    private Page<DayOff> expected;

    private List<DayOff> dayOffs;

    @Before
    public void init() throws IOException {
        initData();
        this.dayOffs = (List<DayOff>) dayOffRepository.findAll();
    }

    @Test
    public void findNonCancelledByAccountId() throws Exception {
        this.dayOffs.remove(2);
        expected = new PageImpl<>(this.dayOffs);
        Page<DayOff> actual =
                dayOffRepository.findNonCancelledByAccountId(1, PageRequest.of(0, 20));
        assertEquals(expected.getTotalElements(), actual.getTotalElements());
    }

    @Test
    public void findByKeyword() throws Exception {
    }
////
//    @Test
//    public void findNonCancelledByKeyword() throws Exception {
//    }


}