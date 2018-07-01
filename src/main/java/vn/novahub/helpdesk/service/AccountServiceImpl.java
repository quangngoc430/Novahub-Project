package vn.novahub.helpdesk.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Service;
import vn.novahub.helpdesk.constant.AccountConstant;
import vn.novahub.helpdesk.constant.RoleConstant;
import vn.novahub.helpdesk.exception.*;
import vn.novahub.helpdesk.model.Account;
import vn.novahub.helpdesk.model.Mail;
import vn.novahub.helpdesk.repository.AccountRepository;
import vn.novahub.helpdesk.validation.*;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
import java.util.Date;

@Service
public class AccountServiceImpl implements AccountService {

    @Autowired
    private Environment env;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    private TokenService tokenService;

    @Autowired
    private RoleService roleService;

    @Autowired
    private AccountValidation accountValidation;

    @Autowired
    private MailService mailService;

    @Override
    public boolean isAccountLogin(long accountId) {
        Account account = getAccountLogin();
        return (account.getId() == accountId);
    }

    @Override
    public Account getAccountLogin() {
        String email = ((User)SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername();
        return accountRepository.getByEmail(email);
    }

    @Override
    public Account getByEmail(String email) {
        return accountRepository.getByEmail(email);
    }

    @Override
    public boolean activateAccount(long accountId, String token) {
        Account account = accountRepository.getByIdAndToken(accountId, token);

        if(account == null) {
            return false;
        }

        account.setStatus(AccountConstant.STATUS_ACTIVE);
        account.setVertificationToken(null);
        accountRepository.save(account);

        return true;
    }

    @Override
    public Account updateToken(Account account, String token) {
        account.setToken(token);
        return accountRepository.save(account);
    }

    @Override
    public Account login(Account accountInput, HttpServletRequest request) throws AccountInvalidException, AccountInactiveException, AccountLockedException, AccountValidationException {

        accountValidation.validateAccount(accountInput, GroupLoginAccount.class);

        Account account = accountRepository.getByEmail(accountInput.getEmail());

        if(account.getPassword() == null || !bCryptPasswordEncoder.matches(accountInput.getPassword(), account.getPassword()))
            throw new AccountInvalidException();

        if(account.getStatus().equals(AccountConstant.STATUS_INACTIVE))
            throw new AccountInactiveException(account.getEmail());

        if(account.getStatus().equals(AccountConstant.STATUS_LOCKED))
            throw new AccountLockedException(account.getEmail());

        UserDetails userDetails = new User(account.getEmail(), account.getPassword(), account.getAuthorities());
        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails, null,
                userDetails.getAuthorities());
        authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        SecurityContextHolder.getContext().setAuthentication(authentication);

        return account;
    }

    @Override
    public Page<Account> getAll(String keyword, Pageable pageable) {
        keyword = "%" + keyword + "%";
        return accountRepository.getAllByEmailLikeAndFirstNameLikeAndLastNameLike(keyword, keyword, keyword, pageable);
    }

    @Override
    public Account get(long accountId) throws AccountNotFoundException {

        Account account = accountRepository.getById(accountId);

        if (account == null)
            throw new AccountNotFoundException(accountId);

        return account;
    }

    @Override
    public Account create(Account account) throws AccountIsExistException, RoleNotFoundException, AccountValidationException, MessagingException {

        accountValidation.validateAccount(account, GroupCreateAccount.class);

        if(accountRepository.getByEmail(account.getEmail()) != null)
            throw new AccountIsExistException(account.getEmail());

        account.setPassword(bCryptPasswordEncoder.encode(account.getPassword()));
        account.setTotalNumberOfHours(AccountConstant.TOTAL_NUMBER_OF_HOURS_DEFAULT);
        account.setRemainNumberOfHours(AccountConstant.REMAIN_NUMBER_OF_HOURS_DEFAULT);
        account.setStatus(AccountConstant.STATUS_INACTIVE);
        account.setVertificationToken(tokenService.generateToken(account.getEmail() + account.getEmail()));
        account.setRoleId(roleService.getByName(RoleConstant.ROLE_USER).getId());
        account.setCreatedAt(new Date());
        account.setUpdatedAt(new Date());

        account = accountRepository.save(account);

        Mail mail = new Mail();
        mail.setEmailReceiving(account.getEmail());
        mail.setSubject(env.getProperty("subject_email_sign_up"));
        String urlAccountActive = "http://localhost:8080/api/users/" + account.getId() + "/active?token=" + account .getVertificationToken();
        String contentEmailSignUp = env.getProperty("content_email_sign_up");
        contentEmailSignUp = contentEmailSignUp.replace("{url-activate-account}", urlAccountActive);
        mail.setContent(contentEmailSignUp);
        mailService.sendHTMLMail(mail);

        return account;
    }

    @Override
    @Transactional
    public Account createWithGoogleAccount(Account account) throws AccountValidationException, AccountIsExistException, RoleNotFoundException {

        accountValidation.validateAccount(account, GroupCreateWithAccountGoogle.class);

        if(accountRepository.getByEmail(account.getEmail()) != null)
            throw new AccountIsExistException(account.getEmail());

        account.setTotalNumberOfHours(AccountConstant.TOTAL_NUMBER_OF_HOURS_DEFAULT);
        account.setRemainNumberOfHours(AccountConstant.REMAIN_NUMBER_OF_HOURS_DEFAULT);
        account.setStatus(AccountConstant.STATUS_ACTIVE);
        account.setRoleId(roleService.getByName(RoleConstant.ROLE_USER).getId());
        account.setPassword(null);
        account.setToken(null);
        account.setCreatedAt(new Date());
        account.setUpdatedAt(new Date());

        account = accountRepository.save(account);

        return account;
    }

    @Override
    public Account update(Account account) throws AccountValidationException, AccountPasswordNotEqualException {

        Account oldAccount = getAccountLogin();

        // check changing password
        if(oldAccount.getPassword() != null){
            if(account.getNewPassword() != null || account.getPassword() != null){
                if(!bCryptPasswordEncoder.matches(account.getPassword(), oldAccount.getPassword()))
                    throw new AccountPasswordNotEqualException("Password do not match");

                oldAccount.setPassword(bCryptPasswordEncoder.encode(account.getNewPassword()));
            }
        } else {
            if(account.getPassword() != null)
                oldAccount.setPassword(bCryptPasswordEncoder.encode(account.getPassword()));
        }

        if(account.getFirstName() != null)
            oldAccount.setFirstName(account.getFirstName());
        if(account.getLastName() != null)
            oldAccount.setLastName(account.getLastName());
        if(account.getDayOfBirth() != null)
            oldAccount.setDayOfBirth(account.getDayOfBirth());
        if(account.getAddress() != null)
            oldAccount.setAddress(account.getAddress());
        if(account.getAvatarUrl() != null)
            oldAccount.setAvatarUrl(account.getAvatarUrl());
        oldAccount.setUpdatedAt(new Date());

        accountValidation.validateAccount(account, GroupUpdateAccount.class);

        return accountRepository.save(oldAccount);
    }

    @Override
    public Account updatedForAdmin(long accountId, Account account) throws AccountValidationException {
        Account oldAccount = accountRepository.getById(accountId);

        // check changing password
        if(account.getPassword() != null){
            oldAccount.setPassword(bCryptPasswordEncoder.encode(account.getPassword()));
        }

        if(account.getFirstName() != null)
            oldAccount.setFirstName(account.getFirstName());
        if(account.getLastName() != null)
            oldAccount.setLastName(account.getLastName());
        if(account.getDayOfBirth() != null)
            oldAccount.setDayOfBirth(account.getDayOfBirth());
        if(account.getAddress() != null)
            oldAccount.setAddress(account.getAddress());
        if(account.getAvatarUrl() != null)
            oldAccount.setAvatarUrl(account.getAvatarUrl());
        if(account.getStatus() != null)
            oldAccount.setStatus(account.getStatus());
        oldAccount.setUpdatedAt(new Date());

        accountValidation.validateAccount(account, GroupUpdateAccount.class);

        return accountRepository.save(oldAccount);
    }

    @Override
    public void delete(long accountId) throws AccountNotFoundException {

        if(accountRepository.getById(accountId) == null)
            throw new AccountNotFoundException(accountId);

        accountRepository.deleteById(accountId);
    }

}
