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
import vn.novahub.helpdesk.model.Account;
import vn.novahub.helpdesk.model.Issue;
import vn.novahub.helpdesk.model.Role;

import java.io.IOException;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class IssueRepositoryTest extends BaseRepositoryTest {

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private IssueRepository issueRepository;

    @Before
    public void before() throws IOException {
        initData();
    }

    @Test
    public void testGetAllByTitleContainingOrContentContaining() {
        Page<Issue> actual = issueRepository.getAllByTitleContainingOrContentContaining("", "", new PageRequest(0, 20));
        Page<Issue> expected = new PageImpl<>(IteratorUtils.toList(issueRepository.findAll().iterator()));

        assertEquals(actual.getTotalElements(), expected.getTotalElements());
        assertEquals(actual.getContent().get(0).getTitle(), expected.getContent().get(0).getTitle());
    }

    @Test
    public void testGetAllByTitleContainingOrContentContainingAndStatus() {
        Page<Issue> actual = issueRepository.getAllByTitleContainingOrContentContainingAndStatus("", "", "PENDING", new PageRequest(0, 20));
        Page<Issue> expected = new PageImpl<>(IteratorUtils.toList(issueRepository.findAll().iterator()));

        assertEquals(actual.getTotalElements(), expected.getTotalElements());
        assertEquals(actual.getContent().get(0).getTitle(), expected.getContent().get(0).getTitle());
    }

    @Test
    public void testGetAllByAccountIdAndTitleContainingOrContentContaining() {
        Page<Issue> actual = issueRepository.getAllByAccountIdAndTitleContainingOrContentContaining(1, "", new PageRequest(0, 20));
        List<Issue> issues = IteratorUtils.toList(issueRepository.findAll().iterator());

        for (int i = issues.size() - 1; i >= 0; i--) {
            if (issues.get(i).getAccountId() != 1)
                issues.remove(i);
        }

        Page<Issue> expected = new PageImpl<>(issues);

        assertEquals(actual.getTotalElements(), expected.getTotalElements());
        assertEquals(actual.getContent().get(0).getTitle(), expected.getContent().get(0).getTitle());
    }

    @Test
    public void testGetAllByAccountIdAndStatusAndTitleContainingOrContentContaining() {
        Page<Issue> actual = issueRepository.getAllByAccountIdAndStatusAndTitleContainingAndContentContaining(1, "", "PENDING", new PageRequest(0, 20));
        List<Issue> issues = IteratorUtils.toList(issueRepository.findAll().iterator());

        for (int i = issues.size() - 1; i >= 0; i--) {
            if (issues.get(i).getAccountId() != 1)
                issues.remove(i);
        }

        Page<Issue> expected = new PageImpl<>(issues);

        assertEquals(actual.getTotalElements(), expected.getTotalElements());
        assertEquals(actual.getContent().get(0).getTitle(), expected.getContent().get(0).getTitle());
    }

    @Test
    public void testGetByIdAndStatusIsNot() {
        Issue actual = issueRepository.getByIdAndStatusIsNot(1, "PENDING");

        assertNull(actual);
    }

    @Test
    public void testGetByIdAndAccountIdAndStatusIsNot() {
        Issue actual = issueRepository.getByIdAndAccountIdAndStatusIsNot(44, 11, "CANCELLED" );

        assertEquals(actual.getTitle(), "Xin cty sponsor to chuc party");
    }

    private void initData() throws IOException {
        roleRepository.saveAll(convertJsonFileToObjectList(
                "seeding/roles.json",
                new TypeReference<List<Role>>(){}));

        accountRepository.saveAll(convertJsonFileToObjectList(
                "seeding/accounts.json",
                new TypeReference<List<Account>>(){}));

        issueRepository.saveAll(convertJsonFileToObjectList(
                "seeding/issues.json",
                new TypeReference<List<Issue>>(){}));
    }
}
