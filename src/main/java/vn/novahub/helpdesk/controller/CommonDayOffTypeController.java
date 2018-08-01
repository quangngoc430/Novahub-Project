package vn.novahub.helpdesk.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import vn.novahub.helpdesk.exception.CommonTypeIsNotExistException;
import vn.novahub.helpdesk.model.CommonDayOffType;
import vn.novahub.helpdesk.service.CommonDayOffTypeService;
import java.util.List;

@RestController
@RequestMapping(path = "/api", produces = {MediaType.APPLICATION_JSON_VALUE})
public class CommonDayOffTypeController {

    @Autowired
    private CommonDayOffTypeService commonDayOffTypeService;

    @PreAuthorize("isAuthenticated()")
    @GetMapping(path = "/common-day-off-types")
    public ResponseEntity<List<CommonDayOffType>> getAllType() {

        List<CommonDayOffType> commonDayOffTypes = commonDayOffTypeService.getAllCommonType();

        return new ResponseEntity<>(commonDayOffTypes, HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping(path = "/admin/common-day-off-types")
    public ResponseEntity<CommonDayOffType> create(@RequestBody CommonDayOffType newCommonDayOffType) {

        CommonDayOffType commonDayOffType = commonDayOffTypeService.create(newCommonDayOffType);

        return new ResponseEntity<>(commonDayOffType, HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PutMapping(path = "/admin/common-day-off-types")
    public ResponseEntity<CommonDayOffType> update(@RequestBody CommonDayOffType newCommonDayOffType)
                                                    throws CommonTypeIsNotExistException{

        CommonDayOffType commonDayOffType = commonDayOffTypeService.update(newCommonDayOffType);

        return new ResponseEntity<>(commonDayOffType, HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @DeleteMapping(path = "/admin/common-day-off-types")
    public ResponseEntity<CommonDayOffType> update(@RequestParam("id") int commonTypeId)
                                                    throws CommonTypeIsNotExistException{

        CommonDayOffType commonDayOffType = commonDayOffTypeService.delete(commonTypeId);

        return new ResponseEntity<>(commonDayOffType, HttpStatus.OK);
    }

}
