package vn.novahub.helpdesk.repository;

import org.assertj.core.api.Assertions;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
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
        role.setName("test");
        role = repository.save(role);

        Role r = repository.getById(role.getId());
        Assertions.assertThat(r.getId()).isEqualTo(role.getId());
        Assertions.assertThat(r.getName()).isEqualTo("test");
    }

    @Test
    public void testGetARoleByName() {
        Role role = new Role();
        role.setName("ADMIN");
        repository.save(role);

        Role r = repository.getByName("ADMIN");
        Assertions.assertThat(r.getName()).isEqualTo("ADMIN");
    }

    @Test
    public void testGetAllByNameContaining() {
        Role role = new Role();
        role.setName("ADMIN");
        repository.save(role);

        role = new Role();
        role.setName("CLERK");
        repository.save(role);

        role = new Role();
        role.setName("EMPLOYEE");
        repository.save(role);

        Page<Role> roles = repository.getAllByNameContaining("AD", PageRequest.of(0, 20));
        Assertions.assertThat(roles.getContent().get(0).getName()).isEqualTo("ADMIN");
        Assertions.assertThat(roles.getContent().size()).isEqualTo(1);
    }

    @Test
    public void testGetRoleNotFound() {
        Role role = repository.getById(1000L);
        Assert.assertNull(role);
    }
}
