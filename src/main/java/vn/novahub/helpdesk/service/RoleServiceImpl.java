package vn.novahub.helpdesk.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import vn.novahub.helpdesk.model.Role;
import vn.novahub.helpdesk.repository.RoleRepository;

import javax.servlet.http.HttpServletRequest;

@Service
public class RoleServiceImpl implements RoleService {

    @Autowired
    private RoleRepository roleRepository;

    @Override
    public Role getById(long roleId, HttpServletRequest request) {

        Role role = roleRepository.getById(roleId);

        if (role == null)

        return null;
    }

    @Override
    public Role getByName(String roleName, HttpServletRequest request) {
        return null;
    }

    @Override
    public Page<Role> getAllByName(String roleName, HttpServletRequest request) {
        return null;
    }

}
