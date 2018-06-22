package vn.novahub.helpdesk.service;

import org.springframework.data.domain.Page;
import vn.novahub.helpdesk.model.Role;

import javax.servlet.http.HttpServletRequest;

public interface RoleService {

    Role getById(long roleId, HttpServletRequest request);

    Role getByName(String roleName, HttpServletRequest request);

    Page<Role> getAllByName(String roleName, HttpServletRequest request);
}
