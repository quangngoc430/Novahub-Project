package vn.novahub.helpdesk.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import vn.novahub.helpdesk.exception.*;
import vn.novahub.helpdesk.model.Account;
import vn.novahub.helpdesk.model.Token;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

public interface AccountService {

    boolean isAccountLogin(long accountId);

    void authenticationToken(String token, HttpServletRequest request) throws TokenIsExpiredException, UnauthorizedException;

    Account getAccountLogin();

    Account getByEmail(String email);

    boolean activateAccount(long accountId, String token);

    Token login(Account account, HttpServletRequest request) throws AccountInvalidException, AccountInactiveException, AccountLockedException, AccountValidationException;

    void logout(String token) throws TokenNotFoundException;

    Account loginWithGoogle(String code, HttpServletRequest request) throws IOException, EmailFormatException, RoleNotFoundException, AccountIsExistException, AccountValidationException;

    Page<Account> getAll(String keyword, String status, String role, Pageable pageable);

    Account get(long accountId) throws AccountNotFoundException;

    Account create(Account account) throws AccountIsExistException, RoleNotFoundException, AccountValidationException, MessagingException;

    Account createWithGoogleAccount(Account account) throws AccountValidationException, AccountIsExistException, RoleNotFoundException;

    Account update(Account account) throws AccountValidationException, AccountPasswordNotEqualException;

    Account updatedForAdmin(long accountId, Account account) throws AccountValidationException, AccountNotFoundException;

    void delete(long accountId) throws AccountNotFoundException;
}
