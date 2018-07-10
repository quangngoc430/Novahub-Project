package vn.novahub.helpdesk.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import vn.novahub.helpdesk.model.Role;

@Repository
public interface RoleRepository extends PagingAndSortingRepository<Role, Long> {

    Role getById(long roleId);

    Role getByName(String roleName);

    Page<Role> getAllByNameLike(String roleName, Pageable pageable);
}
