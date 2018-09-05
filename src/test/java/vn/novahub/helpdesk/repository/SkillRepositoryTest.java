package vn.novahub.helpdesk.repository;

import com.fasterxml.jackson.core.type.TypeReference;
import org.apache.commons.collections.IteratorUtils;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.test.annotation.DirtiesContext;
import vn.novahub.helpdesk.model.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class SkillRepositoryTest extends BaseRepositoryTest {

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private SkillRepository skillRepository;

    @Autowired
    private AccountHasSkillRepository accountHasSkillRepository;

    @Before
    public void before() throws IOException {
        initData();
    }

    @Test
    public void testGetAllByNameContainingAndAccountId() {
        List<Skill> skills = IteratorUtils.toList(skillRepository.findAll().iterator());
        List<AccountHasSkill> accountHasSkills = IteratorUtils.toList(accountHasSkillRepository.findAll().iterator());
        List<Category> categories = IteratorUtils.toList(categoryRepository.findAll().iterator());

        for (int i = accountHasSkills.size() - 1; i >= 0; i--) {
            if (accountHasSkills.get(i).getAccountId() != 1) {
                accountHasSkills.remove(i);
            }
        }

        boolean check;

        for (int i = skills.size() - 1; i >= 0; i--) {
            check = false;
            for (int j = accountHasSkills.size() - 1; j >= 0; j--) {
                if (skills.get(i).getId() == accountHasSkills.get(j).getSkillId()) {
                    skills.get(i).setLevel(accountHasSkills.get(j).getLevel());
                    check = true;
                    for (int z = categories.size() - 1; z >= 0; z--) {
                        if (skills.get(i).getCategoryId() == categories.get(z).getId()) {
                            skills.get(i).setCategory(categories.get(z));
                            break;
                        }
                    }
                    accountHasSkills.remove(j);
                    break;
                }
            }

            if (!check)
                skills.remove(i);
        }

        Page<Skill> expected = new PageImpl<>(skills);
        Page<Skill> actual = skillRepository.getAllByNameContainingAndAccountId("", 1L, new PageRequest(0, 20));

        assertEquals(actual.getTotalElements(), expected.getTotalElements());
        assertEquals(actual.getContent().get(0).getName(), expected.getContent().get(0).getName());
        assertEquals(actual.getContent().get(0).getLevel(), expected.getContent().get(0).getLevel());
    }

    @Test
    public void testGetAllByNameContainingAndCategoryId() {
        List<Skill> skills = IteratorUtils.toList(skillRepository.findAll().iterator());

        for (int i = skills.size() - 1; i >= 0; i--) {
            if (skills.get(i).getCategoryId() != 1) {
                skills.remove(i);
            }
        }

        Page<Skill> expected = new PageImpl<>(skills);
        Page<Skill> actual = skillRepository.getAllByNameContainingAndCategoryId("", 1, new PageRequest(0, 20, new Sort(Sort.Direction.ASC, "id")));

        assertEquals(actual.getTotalElements(), expected.getTotalElements());
        assertEquals(actual.getContent().get(0).getName(), expected.getContent().get(0).getName());
        assertEquals(actual.getContent().get(0).getLevel(), expected.getContent().get(0).getLevel());
    }

    @Test
    public void testGetByNameAndCategoryId() {
        Skill actual = skillRepository.getByNameAndCategoryId("Python", 8);

        assertEquals("Python", actual.getName());
        assertEquals(8, actual.getCategoryId());
    }

    @Test
    public void testGetByIdAndCategoryId() {
        Skill actual = skillRepository.getByIdAndCategoryId(37, 8);

        assertEquals(37, actual.getId());
        assertEquals(8, actual.getCategoryId());
        assertEquals("Ruby", actual.getName());
    }

    @Test
    public void testExistsByIdAndCategoryId() {
        assertTrue(skillRepository.existsByIdAndCategoryId(35, 8));
        assertFalse(skillRepository.existsByIdAndCategoryId(23, 9));
    }

    @Test
    public void testGetByAccountIdAndSkillId() {
        List<Skill> skills = IteratorUtils.toList(skillRepository.findAll().iterator());
        List<AccountHasSkill> accountHasSkills = IteratorUtils.toList(accountHasSkillRepository.findAll().iterator());
        List<Category> categories = IteratorUtils.toList(categoryRepository.findAll().iterator());

        Skill expected = new Skill();

        for (Skill skill : skills) {
            if (skill.getId() == 1) {
                expected = skill;
                break;
            }
        }

        for (AccountHasSkill accountHasSkill : accountHasSkills) {
            if (accountHasSkill.getSkillId() == 1 && accountHasSkill.getAccountId() == 1) {
                expected.setLevel(accountHasSkill.getLevel());
                break;
            }
        }

        for (Category category : categories) {
            if (category.getId() == expected.getCategoryId()) {
                expected.setCategory(category);
                break;
            }
        }

        Skill actual = skillRepository.getByAccountIdAndSkillId(1, 1);

        assertEquals(actual.getName(), expected.getName());
        assertEquals(actual.getLevel(), expected.getLevel());
        assertEquals(actual.getCategoryId(), expected.getCategoryId());

    }

    @Test
    public void testGetByAccountIdAndSkillIdAndCategoryId() {
        List<Skill> skills = IteratorUtils.toList(skillRepository.findAll().iterator());
        List<AccountHasSkill> accountHasSkills = IteratorUtils.toList(accountHasSkillRepository.findAll().iterator());
        List<Category> categories = IteratorUtils.toList(categoryRepository.findAll().iterator());

        Skill expected = new Skill();

        for (Skill skill : skills) {
            if (skill.getId() == 11) {
                expected = skill;
                break;
            }
        }

        for (AccountHasSkill accountHasSkill : accountHasSkills) {
            if (accountHasSkill.getSkillId() == 11 && accountHasSkill.getAccountId() == 2) {
                expected.setLevel(accountHasSkill.getLevel());
                break;
            }
        }

        for (Category category : categories) {
            if (category.getId() == 3) {
                expected.setCategory(category);
                break;
            }
        }

        Skill actual = skillRepository.getByAccountIdAndSkillIdAndCategoryId(2, 11, 3);

        assertEquals(actual.getName(), expected.getName());
        assertEquals(actual.getLevel(), expected.getLevel());
        assertEquals(actual.getCategoryId(), expected.getCategoryId());
    }

    @Test
    public void testGetAllByAccountId() {
        List<Skill> skills = IteratorUtils.toList(skillRepository.findAll().iterator());
        List<AccountHasSkill> accountHasSkills = IteratorUtils.toList(accountHasSkillRepository.findAll().iterator());
        List<Category> categories = IteratorUtils.toList(categoryRepository.findAll().iterator());

        for (int i = accountHasSkills.size() - 1; i >= 0; i--) {
            if (accountHasSkills.get(i).getAccountId() != 1) {
                accountHasSkills.remove(i);
            }
        }

        boolean check;

        for (int i = skills.size() - 1; i >= 0; i--) {
            check = false;
            for (int j = accountHasSkills.size() - 1; j >= 0; j--) {
                if (skills.get(i).getId() == accountHasSkills.get(j).getSkillId()) {
                    skills.get(i).setLevel(accountHasSkills.get(j).getLevel());
                    check = true;
                    for (int z = categories.size() - 1; z >= 0; z--) {
                        if (skills.get(i).getCategoryId() == categories.get(z).getId()) {
                            skills.get(i).setCategory(categories.get(z));
                            break;
                        }
                    }
                    accountHasSkills.remove(j);
                    break;
                }
            }

            if (!check)
                skills.remove(i);
        }

        Page<Skill> expected = new PageImpl<>(skills);
        Page<Skill> actual = skillRepository.getAllByAccountId( 1L, new PageRequest(0, 20));

        assertEquals(actual.getTotalElements(), expected.getTotalElements());
        assertEquals(actual.getContent().get(0).getName(), expected.getContent().get(0).getName());
        assertEquals(actual.getContent().get(0).getLevel(), expected.getContent().get(0).getLevel());
    }

    @Test
    public void testGetAllByIsIn() {
        List<Long> skillIds = new ArrayList<>();
        skillIds.add(1L);
        skillIds.add(4L);
        skillIds.add(7L);

        List<Skill> skills = IteratorUtils.toList(skillRepository.findAll().iterator());
        boolean check;

        for (int i = skills.size() - 1; i >= 0; i--) {
            check = false;
            for (int j = skillIds.size() - 1; j >= 0; j--) {
                if (skills.get(i).getId() == skillIds.get(j)) {
                    check = true;
                    break;
                }
            }
            if (!check)
                skills.remove(i);
        }

        Page<Skill> actual = skillRepository.getAllByIdIsIn(skillIds, new PageRequest(0, 20));
        Page<Skill> expected = new PageImpl<>(skills);

        assertEquals(actual.getTotalElements(), expected.getTotalElements());
        assertEquals(actual.getContent().get(0).getName(), expected.getContent().get(0).getName());
    }

    private void initData() throws IOException {
        roleRepository.saveAll(convertJsonFileToObjectList(
                "seeding/roles.json",
                new TypeReference<List<Role>>() {}));

        accountRepository.saveAll(convertJsonFileToObjectList(
                "seeding/accounts.json",
                new TypeReference<List<Account>>() {}));

        categoryRepository.saveAll(convertJsonFileToObjectList(
                "seeding/categories.json",
                new TypeReference<List<Category>>() {}));

        skillRepository.saveAll(convertJsonFileToObjectList(
                "seeding/skills.json",
                new TypeReference<List<Skill>>() {}));

        accountHasSkillRepository.saveAll(convertJsonFileToObjectList(
                "seeding/accountHasSkills.json",
                new TypeReference<List<AccountHasSkill>>() {}));
    }
}
