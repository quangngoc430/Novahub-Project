package vn.novahub.helpdesk.repository;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import vn.novahub.helpdesk.model.Role;

@Repository
public interface RoleRepository extends PagingAndSortingRepository<Role, Long> {

    Role getById(long roleId);

    Role getByName(String roleName);
}
