package vn.novahub.helpdesk.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import vn.novahub.helpdesk.exception.DayOffTypeExistException;
import vn.novahub.helpdesk.exception.DayOffTypeNotFoundException;
import vn.novahub.helpdesk.model.DayOffType;
import vn.novahub.helpdesk.service.DayOffTypeService;

@RestController
@RequestMapping(path = "/api/admin/day-off-types", produces = {MediaType.APPLICATION_JSON_VALUE})
public class DayOffTypeAdminController {

    @Autowired
    private DayOffTypeService dayOffTypeService;


    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping
    public ResponseEntity<DayOffType> create(@RequestBody DayOffType newDayOffType)
                                                                    throws DayOffTypeExistException {

        DayOffType dayOffType = dayOffTypeService.create(newDayOffType);

        return new ResponseEntity<>(dayOffType, HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<DayOffType> update(@RequestBody DayOffType newDayOffType,
                                             @PathVariable("id") int id)
                                                    throws DayOffTypeNotFoundException {
        newDayOffType.setId(id);
        DayOffType dayOffType = dayOffTypeService.update(newDayOffType);
        return new ResponseEntity<>(dayOffType, HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<DayOffType> delete(@PathVariable("id") int id)
                                                    throws DayOffTypeNotFoundException {

        DayOffType dayOffType = dayOffTypeService.delete(id);

        return new ResponseEntity<>(dayOffType, HttpStatus.OK);
    }

}
