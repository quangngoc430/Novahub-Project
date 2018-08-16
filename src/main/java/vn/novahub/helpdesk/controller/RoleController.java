package vn.novahub.helpdesk.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import vn.novahub.helpdesk.exception.RoleNotFoundException;
import vn.novahub.helpdesk.model.Role;
import vn.novahub.helpdesk.service.RoleService;

@RestController
@RequestMapping(path = "/api", produces = {MediaType.APPLICATION_JSON_VALUE})
public class RoleController {

    @Autowired
    private RoleService roleService;

    @PreAuthorize("isAuthenticated()")
    @GetMapping(path = "/roles/{id}")
    public ResponseEntity<Role> get(@PathVariable("id") long roleId) throws RoleNotFoundException {

        Role role = roleService.getById(roleId);

        return new ResponseEntity<>(role, HttpStatus.OK);
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping(path = "/roles")
    public ResponseEntity<Page<Role>> getAll(@RequestParam(value = "keyword", required = false, defaultValue = "") String roleName,
                                             Pageable pageable) {
        return new ResponseEntity<>(roleService.getAll(roleName, pageable), HttpStatus.OK);
    }
}
