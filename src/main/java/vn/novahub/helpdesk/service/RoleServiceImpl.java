package vn.novahub.helpdesk.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import vn.novahub.helpdesk.exception.RoleNotFoundException;
import vn.novahub.helpdesk.model.Role;
import vn.novahub.helpdesk.repository.RoleRepository;

import javax.servlet.http.HttpServletRequest;

@Service
public class RoleServiceImpl implements RoleService {

    @Autowired
    private RoleRepository roleRepository;

    @Override
    public Role getById(long roleId) throws RoleNotFoundException {

        Role role = roleRepository.getById(roleId);

        if (role == null)
            throw new RoleNotFoundException(roleId);

        return role;
    }

    @Override
    public Role getByName(String roleName) throws RoleNotFoundException {

        Role role = roleRepository.getByName(roleName);

        if(role == null)
            throw new RoleNotFoundException(roleName);

        return role;
    }

    @Override
    public Page<Role> getAll(Pageable pageable) {

        return roleRepository.findAll(pageable);
    }

}
