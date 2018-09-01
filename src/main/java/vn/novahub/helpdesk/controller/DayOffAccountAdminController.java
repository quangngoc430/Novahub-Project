package vn.novahub.helpdesk.controller;

import com.fasterxml.jackson.annotation.JsonView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import vn.novahub.helpdesk.exception.DayOffAccountIsExistException;
import vn.novahub.helpdesk.exception.DayOffAccountNotFoundException;
import vn.novahub.helpdesk.exception.DayOffTypeNotFoundException;
import vn.novahub.helpdesk.model.DayOffAccount;
import vn.novahub.helpdesk.service.DayOffAccountService;
import vn.novahub.helpdesk.view.View;

@RestController
@RequestMapping(path = "/api/admin/day-off-accounts", produces = {MediaType.APPLICATION_JSON_VALUE})
public class DayOffAccountAdminController {

    @Autowired
    private DayOffAccountService dayOffAccountService;


    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @JsonView(View.DayOffAccountRespond.class)
    @PostMapping
    public ResponseEntity<DayOffAccount> create(@RequestBody DayOffAccount dayOffAccount)
            throws DayOffAccountIsExistException, DayOffTypeNotFoundException {

        return new ResponseEntity<>(dayOffAccountService.add(dayOffAccount), HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @JsonView(View.DayOffAccountRespond.class)
    @GetMapping
    public ResponseEntity<Page<DayOffAccount>> adminGet(
         @RequestParam(name = "accountId", required = false, defaultValue = "0") String accountIdString,
         Pageable pageable) throws DayOffAccountIsExistException,
                                        DayOffTypeNotFoundException {

        long accountId = Long.parseLong(accountIdString);

        if (accountId != 0) {
            return new ResponseEntity<>(
                    dayOffAccountService.findByAccountId(accountId, pageable),
                    HttpStatus.OK);
        }

        return new ResponseEntity<>(
                dayOffAccountService.getAll(pageable),
                HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @JsonView(View.DayOffAccountRespond.class)
    @PutMapping("/{id}")
    public ResponseEntity<DayOffAccount> update(@RequestBody DayOffAccount dayOffAccount,
                                                @PathVariable("id") long id)
                                                      throws DayOffAccountNotFoundException {
        dayOffAccount.setId(id);
        dayOffAccount = dayOffAccountService.update(dayOffAccount);
        return new ResponseEntity<>(dayOffAccount, HttpStatus.OK);
    }


}
