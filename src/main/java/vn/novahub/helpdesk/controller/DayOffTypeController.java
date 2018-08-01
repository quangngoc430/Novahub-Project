package vn.novahub.helpdesk.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import vn.novahub.helpdesk.exception.DayOffTypeIsExistException;
import vn.novahub.helpdesk.model.Account;
import vn.novahub.helpdesk.model.DayOffType;
import vn.novahub.helpdesk.service.AccountService;
import vn.novahub.helpdesk.service.DayOffTypeService;

@RestController
@RequestMapping(path = "/api", produces = {MediaType.APPLICATION_JSON_VALUE})
public class DayOffTypeController {

    @Autowired
    private DayOffTypeService dayOffTypeService;

    @Autowired
    private AccountService accountService;

    @PreAuthorize("isAuthenticated()")
    @GetMapping(path = "/day-off-types")
    public ResponseEntity<Page<DayOffType>> getUserLoginDayOffType(Pageable pageable) {
        Account account = accountService.getAccountLogin();

        return new ResponseEntity<>(
                dayOffTypeService.findByAccountId(account.getId(), pageable),
                HttpStatus.OK);
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping(path = "/day-off-types")
    public ResponseEntity<DayOffType> create(@RequestBody DayOffType dayOffType)
            throws DayOffTypeIsExistException {
        return new ResponseEntity<>(dayOffTypeService.add(dayOffType), HttpStatus.OK);
    }

//
//    @GetMapping(path = "/day-off-types/{account-id}")
//    public ResponseEntity<Page<DayOffType>> getByAccountId(@PathVariable("account-id") long accountId,
//                                                           Pageable pageable)
//                                                                  throws DayOffTypeNotFoundException {
//
//        Page<DayOffType> dayOffTypes = dayOffTypeService.findByAccountId(accountId, pageable);
//
//        return new ResponseEntity<>(dayOffTypes, HttpStatus.OK);
//    }
//
//    @PostMapping(path = "/day-off-types")
//    public ResponseEntity<String> create(@RequestBody DayOffType dayOffType)
//            throws DayOffTypeIsExistException, DayOffTypeIsNotValidException {
//
//        dayOffTypeService.add(dayOffType);
//
//        return new ResponseEntity<>("Adding new Day off type successful", HttpStatus.OK);
//
//    }
//
//    @PutMapping(path = "/day-off-types")
//    public ResponseEntity<String> update(@RequestBody DayOffType dayOffType,
//                                                   HttpServletRequest request)
//                                                   throws DayOffTypeNotFoundException {
//
//        dayOffTypeService.update(dayOffType);
//
//        return new ResponseEntity<>("Updating day off type successful", HttpStatus.OK);
//    }
//
//    @DeleteMapping(path = "/day-off-types/{type-id}")
//    public ResponseEntity<String> delete(@PathVariable("type-id") long typeId) throws DayOffTypeNotFoundException {
//
//        DayOffType dayOffType = dayOffTypeService.getById(typeId);
//
//        dayOffTypeService.delete(dayOffType);
//
//        return new ResponseEntity<>("Deleting day off type successful", HttpStatus.OK);
//    }


}
