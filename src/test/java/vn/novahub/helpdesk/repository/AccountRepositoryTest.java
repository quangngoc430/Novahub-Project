package vn.novahub.helpdesk.repository;

import com.fasterxml.jackson.core.type.TypeReference;
import org.apache.commons.collections.IteratorUtils;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.annotation.DirtiesContext;
import vn.novahub.helpdesk.model.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class AccountRepositoryTest extends BaseRepositoryTest {

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private AccountHasSkillRepository accountHasSkillRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private SkillRepository skillRepository;

    private Page<Account> expected;

    private Page<Account> actual;

    @Before
    public void before() throws IOException {
        initData();
    }

    @Test
    public void testGetByEmail() {
        Account actual = accountRepository.getByEmail("ngocbui@novahub.vn");
        assertEquals("ngocbui@novahub.vn", actual.getEmail());
    }

    @Test
    public void testGetAllByEmailContainingOrFirstNameContainingOrLastNameContaining() {
        List<Account> accounts = new ArrayList<>();
        accounts.add(accountRepository.getByEmail("breitenberg.ottilie@novahub.vn"));
        expected = new PageImpl<>(accounts);
        actual = accountRepository.getAllByEmailContainingOrFirstNameContainingOrLastNameContaining("breitenberg.ottilie@novahub.vn", new PageRequest(0, 20));

        assertEquals(expected.getContent().get(0), actual.getContent().get(0));
        assertEquals(expected.getContent().size(), actual.getContent().size());
    }

    @Test
    public void testGetAllByEmailContainingOrFirstNameContainingOrLastNameContainingAndStatus() {
        expected = accountRepository.findAll(new PageRequest(0, 20));
        actual = accountRepository.getAllByEmailContainingOrFirstNameContainingOrLastNameContainingAndStatus("", "ACTIVE", new PageRequest(0, 20));

        assertEquals(expected.getContent().get(0), actual.getContent().get(0));
        assertEquals(expected.getContent().size(), actual.getContent().size());
    }

    @Test
    public void testGetAllByEmailContainingOrFirstNameContainingOrLastNameContainingAndRole() {
        List<Account> accounts = new ArrayList<>();
        accounts.add(accountRepository.getByEmail("ngocbui@novahub.vn"));
        expected = new PageImpl<>(accounts);
        actual = accountRepository.getAllByEmailContainingOrFirstNameContainingOrLastNameContainingAndRole("", "CLERK", new PageRequest(0, 20));

        assertEquals(expected.getContent().get(0), actual.getContent().get(0));
        assertEquals(actual.getContent().size(), 1);
    }

    @Test
    public void testGetAllByEmailContainingOrFirstNameContainingOrLastNameContainingAndStatusAndRole() {
        actual = accountRepository.getAllByEmailContainingOrFirstNameContainingOrLastNameContainingAndStatusAndRole("", "INACTIVE","EMPLOYEE", new PageRequest(0, 20));

        assertEquals(actual.getContent().size(), 0);
    }

    @Test
    public void testGetAllByRoleName() {
        List<Account> expected = IteratorUtils.toList(accountRepository.findAll().iterator());
        expected.remove(0);
        expected.remove(0);
        List<Account> actual = accountRepository.getAllByRoleName("EMPLOYEE");

        assertEquals(expected.get(0), actual.get(0));
        assertEquals(expected.size(), actual.size());
    }

    @Test
    public void testGetAllBySkillId() {
        List<AccountHasSkill> accountHasSkills = IteratorUtils.toList(accountHasSkillRepository.findAll().iterator());

        for(int i = accountHasSkills.size() - 1; i >= 0; i--) {
            if (accountHasSkills.get(i).getSkillId() != 1 ) {
                accountHasSkills.remove(i);
            }
        }

        actual = accountRepository.getAllBySkillId(1, new PageRequest(0, 20));

        assertEquals(actual.getTotalElements(), accountHasSkills.size());
    }

    private void initData() throws IOException {
        roleRepository.saveAll(convertJsonFileToObjectList(
                "seeding/roles.json",
                new TypeReference<List<Role>>(){}));

        accountRepository.saveAll(convertJsonFileToObjectList(
                "seeding/accounts.json",
                new TypeReference<List<Account>>(){}));

        categoryRepository.saveAll(convertJsonFileToObjectList(
                "seeding/categories.json",
                new TypeReference<List<Category>>(){}));

        skillRepository.saveAll(convertJsonFileToObjectList(
                "seeding/skills.json",
                new TypeReference<List<Skill>>(){}));

        accountHasSkillRepository.saveAll(convertJsonFileToObjectList(
                "seeding/accountHasSkills.json",
                new TypeReference<List<AccountHasSkill>>(){}));
    }
}
