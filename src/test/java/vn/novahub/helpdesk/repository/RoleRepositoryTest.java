package vn.novahub.helpdesk.repository;

import org.assertj.core.api.Assertions;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import vn.novahub.helpdesk.model.Role;

public class RoleRepositoryTest extends BaseRepositoryTest {

    @Autowired
    private RoleRepository repository;

    @Before
    public void before() {
        repository.deleteAll();
    }

    @After
    public void after() {
        repository.deleteAll();
    }

    @Test
    public void testGetARole() {
        Role role = new Role();
        role.setId(1l);
        role.setName("test");
        repository.save(role);

        Role r = repository.getById(1l);
        Assertions.assertThat(r.getId()).isEqualTo(1l);
        Assertions.assertThat(r.getName()).isEqualTo("test");
    }

    @Test
    public void testGetRoleNotFound() {
        Role role = repository.getById(1000l);
        Assert.assertNull(role);
    }
}
