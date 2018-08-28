package vn.novahub.helpdesk.repository;

import com.fasterxml.jackson.core.type.TypeReference;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.annotation.DirtiesContext;
import vn.novahub.helpdesk.model.*;

import java.io.IOException;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class DayOffRepositoryTest extends BaseRepositoryTest {

    @Autowired
    protected DayOffRepository dayOffRepository;

    @Autowired
    protected DayOffTypeRepository dayOffTypeRepository;

    @Autowired
    protected DayOffAccountRepository dayOffAccountRepository;

    @Autowired
    protected RoleRepository roleRepository;

    @Autowired
    protected AccountRepository accountRepository;

    private Page<DayOff> expected;

    private Page<DayOff> actual;

    private List<DayOff> dayOffs;

    @Before
    public void init() throws IOException {
        initData();
        this.dayOffs = (List<DayOff>) dayOffRepository.findAll();
    }

    @Test
    public void testFindAllByAccountId() throws Exception {
        expected = new PageImpl<>(this.dayOffs);
        actual = dayOffRepository.findAllByAccountId(
                1,
                PageRequest.of(0, 20));
        assertTrue(isEquals(expected, actual));
    }

    @Test
    public void testFindByAccountIdAndStatus() throws Exception {
        this.dayOffs.remove(2);
        this.dayOffs.remove(1);
        expected = new PageImpl<>(this.dayOffs);
        actual = dayOffRepository.findByAccountIdAndStatus(
                1,
                "APPROVED",
                PageRequest.of(0, 20));
        assertTrue(isEquals(expected, actual));
    }

    @Test
    public void testFindNonCancelledByAccountId() throws Exception {
        this.dayOffs.remove(2);
        expected = new PageImpl<>(this.dayOffs);
        actual = dayOffRepository.findNonCancelledByAccountId(
                1,
                PageRequest.of(0, 20));
        assertTrue(isEquals(expected, actual));
    }

    @Test
    public void testFindByKeyword_containingInDayOffType() throws Exception {
        this.dayOffs.remove(1);
        expected = new PageImpl<>(this.dayOffs);
        actual = dayOffRepository.findByKeyword(
                "vacation",
                PageRequest.of(0, 20));
        assertTrue(isEquals(expected, actual));
    }

    @Test
    public void testFindByKeyword_containingInDayOffType_noResult() throws Exception {
        this.dayOffs.remove(2);
        this.dayOffs.remove(1);
        this.dayOffs.remove(0);
        expected = new PageImpl<>(this.dayOffs);
        actual = dayOffRepository.findByKeyword(
                "fadfa",
                PageRequest.of(0, 20));
        assertEquals(expected.getTotalElements(), actual.getTotalElements());

    }

    @Test
    public void testFindByKeyword_containingInEmail() throws Exception {
        expected = new PageImpl<>(this.dayOffs);
        actual = dayOffRepository.findByKeyword(
                "help",
                PageRequest.of(0, 20));
        assertTrue(isEquals(expected, actual));
    }

    @Test
    public void testFindByKeyword_containingInEmail_noResult() throws Exception {
        this.dayOffs.remove(2);
        this.dayOffs.remove(1);
        this.dayOffs.remove(0);
        expected = new PageImpl<>(this.dayOffs);
        actual = dayOffRepository.findByKeyword(
                "ngocbui",
                PageRequest.of(0, 20));
        assertEquals(expected.getTotalElements(), actual.getTotalElements());
    }

    @Test
    public void testFindNonCancelledByKeyword_containingInEmail() throws Exception {
        this.dayOffs.remove(2);
        expected = new PageImpl<>(this.dayOffs);
        actual = dayOffRepository.findNonCancelledByKeyword(
                "help",
                PageRequest.of(0, 20));
        assertTrue(isEquals(expected, actual));
    }

    @Test
    public void testFindNonCancelledByKeyword_containingInEmail_noResult() throws Exception {
        this.dayOffs.remove(2);
        this.dayOffs.remove(1);
        this.dayOffs.remove(0);
        expected = new PageImpl<>(this.dayOffs);
        actual = dayOffRepository.findNonCancelledByKeyword(
                "ngoc",
                PageRequest.of(0, 20));
        assertEquals(expected.getTotalElements(), actual.getTotalElements());
    }

    @Test
    public void testFindNonCancelledByKeyword_containingInDayOffType() throws Exception {
        this.dayOffs.remove(2);
        this.dayOffs.remove(1);
        expected = new PageImpl<>(this.dayOffs);
        actual = dayOffRepository.findNonCancelledByKeyword(
                "vacation",
                PageRequest.of(0, 20));
        assertTrue(isEquals(expected, actual));
    }

    @Test
    public void testFindNonCancelledByKeyword_containingInDayOffType_noResult() throws Exception {
        this.dayOffs.remove(2);
        this.dayOffs.remove(1);
        this.dayOffs.remove(0);
        expected = new PageImpl<>(this.dayOffs);
        actual = dayOffRepository.findNonCancelledByKeyword(
                "fadfadfa",
                PageRequest.of(0, 20));
        assertEquals(expected.getTotalElements(), actual.getTotalElements());
    }

    private <T extends Page> boolean isEquals(T expected, T actual) {
        return expected.getTotalElements() == actual.getTotalElements() &&
                expected.getContent().get(0).equals(actual.getContent().get(0));
    }

    private void initData() throws IOException {

        roleRepository.saveAll(convertJsonFileToObjectList(
                "seeding/roles.json",
                new TypeReference<List<Role>>() {
                }));

        accountRepository.saveAll(convertJsonFileToObjectList(
                "seeding/accounts.json",
                new TypeReference<List<Account>>() {
                }));

        dayOffTypeRepository.saveAll(convertJsonFileToObjectList(
                "seeding/day_off_type.json",
                new TypeReference<List<DayOffType>>() {
                }));

        dayOffAccountRepository.saveAll(convertJsonFileToObjectList(
                "seeding/day_off_account.json",
                new TypeReference<List<DayOffAccount>>() {
                }));

        dayOffRepository.saveAll(convertJsonFileToObjectList(
                "seeding/day_off.json",
                new TypeReference<List<DayOff>>() {
                }));
    }
}