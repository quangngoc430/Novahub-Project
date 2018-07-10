package vn.novahub.helpdesk.controller;

import io.swagger.annotations.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
import vn.novahub.helpdesk.service.LogService;
import vn.novahub.helpdesk.service.RoleService;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping(path = "/api", produces = {MediaType.APPLICATION_JSON_VALUE})
@Api(tags = "Roles Rest Controller")
public class RoleController {

    private static final Logger logger = LoggerFactory.getLogger(RoleController.class);

    @Autowired
    private LogService logService;

    @Autowired
    private RoleService roleService;

    @PreAuthorize("isAuthenticated()")
    @GetMapping(path = "/roles/{id}")
    @ApiOperation(value = "Get role by Id", tags = "Roles Rest Controller")
    @ApiResponses(value = { @ApiResponse(code = 200, message = "Success"),
                            @ApiResponse(code = 404, message = "Role not found")})
    public ResponseEntity<Role> get(@PathVariable("id") long roleId,
                                    HttpServletRequest request) throws RoleNotFoundException {
        logService.log(request, logger);

        Role role = roleService.getById(roleId);

        return new ResponseEntity<>(role, HttpStatus.OK);
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping(path = "/roles")
    @ApiOperation(value = "Get all roles", tags = "Role Rest Controller")
    @ApiResponses(value = { @ApiResponse(code = 200, message = "Success")})
    @ApiImplicitParams(
            value = {
                    @ApiImplicitParam(name = "page", value = "page number start from 0", dataType = "integer",
                            examples = @Example(@ExampleProperty("1")), paramType = "query"),
                    @ApiImplicitParam(name = "size", value = "maximum number of item in page", dataType = "integer",
                            examples = @Example(@ExampleProperty("3")), paramType = "query"),
                    @ApiImplicitParam(name = "sort", allowMultiple = true, value = "sort of item in page", dataType = "string",
                            examples = @Example(@ExampleProperty("name")), paramType = "query")
            }
    )
    public ResponseEntity<Page<Role>> getAll(@RequestParam(value = "keyword", required = false, defaultValue = "") String roleName,
                                            HttpServletRequest request,
                                            Pageable pageable) {
        logService.log(request, logger);

        return new ResponseEntity<>(roleService.getAll(roleName, pageable), HttpStatus.OK);
    }
}
