package vn.novahub.helpdesk.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import vn.novahub.helpdesk.exception.DayOffTypeIsNotExistException;
import vn.novahub.helpdesk.model.DayOffType;
import vn.novahub.helpdesk.service.DayOffTypeService;
import java.util.List;

@RestController
@RequestMapping(path = "/api", produces = {MediaType.APPLICATION_JSON_VALUE})
public class DayOffTypeController {

    @Autowired
    private DayOffTypeService dayOffTypeService;

    @PreAuthorize("isAuthenticated()")
    @GetMapping(path = "/day-off-types")
    public ResponseEntity<List<DayOffType>> getAllType() {

        List<DayOffType> dayOffTypes = dayOffTypeService.getAllDayOffType();

        return new ResponseEntity<>(dayOffTypes, HttpStatus.OK);
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping(path = "/day-off-types/{id}")
    public ResponseEntity<DayOffType> getTypeById(@PathVariable("id") int id)
                                            throws DayOffTypeIsNotExistException {
        return new ResponseEntity<>(dayOffTypeService.getById(id), HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping(path = "/admin/day-off-types")
    public ResponseEntity<DayOffType> create(@RequestBody DayOffType newDayOffType) {

        DayOffType dayOffType = dayOffTypeService.create(newDayOffType);

        return new ResponseEntity<>(dayOffType, HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PutMapping(path = "/admin/day-off-types")
    public ResponseEntity<DayOffType> update(@RequestBody DayOffType newDayOffType)
                                                    throws DayOffTypeIsNotExistException {

        DayOffType dayOffType = dayOffTypeService.update(newDayOffType);

        return new ResponseEntity<>(dayOffType, HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @DeleteMapping(path = "/admin/day-off-types/{id}")
    public ResponseEntity<DayOffType> delete(@PathVariable("id") int id)
                                                    throws DayOffTypeIsNotExistException {

        DayOffType dayOffType = dayOffTypeService.delete(id);

        return new ResponseEntity<>(dayOffType, HttpStatus.OK);
    }

}
