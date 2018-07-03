package vn.novahub.helpdesk.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import vn.novahub.helpdesk.exception.RoleNotFoundException;
import vn.novahub.helpdesk.model.Role;

import javax.servlet.http.HttpServletRequest;

public interface RoleService {

    Role getById(long roleId) throws RoleNotFoundException;

    Role getByName(String roleName) throws RoleNotFoundException;

    Page<Role> getAll(Pageable pageable);
}
