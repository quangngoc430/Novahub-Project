package vn.novahub.helpdesk.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import vn.novahub.helpdesk.exception.*;
import vn.novahub.helpdesk.model.Account;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;

public interface AccountService {

    boolean isAccountLogin(long accountId);

    Account getAccountLogin();

    Account getByEmail(String email);

    boolean activateAccount(long accountId, String token);

    Account updateToken(Account account, String token);

    Account login(Account account, HttpServletRequest request) throws AccountInvalidException, AccountInactiveException, AccountLockedException, AccountValidationException;

    Page<Account> getAll(String keyword, Pageable pageable);

    Account get(long accountId) throws AccountNotFoundException;

    Account create(Account account) throws AccountIsExistException, RoleNotFoundException, AccountValidationException, MessagingException;

    Account createWithGoogleAccount(Account account) throws AccountValidationException, AccountIsExistException, RoleNotFoundException;

    Account update(Account account) throws AccountValidationException, AccountPasswordNotEqualException;

    Account updatedForAdmin(long accountId, Account account) throws AccountValidationException;

    void delete(long accountId) throws AccountNotFoundException;
}
