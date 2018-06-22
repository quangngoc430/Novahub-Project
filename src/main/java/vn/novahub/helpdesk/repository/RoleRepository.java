package vn.novahub.helpdesk.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import vn.novahub.helpdesk.model.Role;

@Repository
public interface RoleRepository extends CrudRepository<Role, Long> {

    Role getById(long roleId);

    Role getByName(String roleName);
}
