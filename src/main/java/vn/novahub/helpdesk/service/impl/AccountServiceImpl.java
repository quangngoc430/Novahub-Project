package vn.novahub.helpdesk.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
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
import vn.novahub.helpdesk.enums.AccountEnum;
import vn.novahub.helpdesk.enums.RoleEnum;
import vn.novahub.helpdesk.exception.*;
import vn.novahub.helpdesk.model.Account;
import vn.novahub.helpdesk.model.GooglePojo;
import vn.novahub.helpdesk.model.Mail;
import vn.novahub.helpdesk.repository.AccountRepository;
import vn.novahub.helpdesk.service.*;
import vn.novahub.helpdesk.validation.*;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import javax.validation.groups.Default;
import java.io.IOException;
import java.util.Date;

@Service
@PropertySource("classpath:email.properties")
public class AccountServiceImpl implements AccountService {

    @Value("${subject_email_sign_up}")
    private String subjectEmailSignUp;

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

    @Autowired
    private GoogleService googleService;

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
    public boolean activateAccount(long accountId, String verificationToken) {
        Account account = accountRepository.getByIdAndVertificationToken(accountId, verificationToken);

        if(account == null) {
            return false;
        }

        account.setStatus(AccountEnum.ACTIVE.name());
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

        accountValidation.validate(accountInput, GroupLoginAccount.class);

        Account account = accountRepository.getByEmail(accountInput.getEmail());

        if(account == null || account.getPassword() == null || !bCryptPasswordEncoder.matches(accountInput.getPassword(), account.getPassword()))
            throw new AccountInvalidException();

        if(account.getStatus().equals(AccountEnum.INACTIVE.name()))
            throw new AccountInactiveException(account.getEmail());

        if(account.getStatus().equals(AccountEnum.LOCKED.name()))
            throw new AccountLockedException(account.getEmail());

        UserDetails userDetails = new User(account.getEmail(), account.getPassword(), account.getAuthorities());
        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails, null,
                userDetails.getAuthorities());
        authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        SecurityContextHolder.getContext().setAuthentication(authentication);

        return account;
    }

    @Override
    public Account loginWithGoogle(String code, HttpServletRequest request) throws EmailFormatException,
            RoleNotFoundException, AccountIsExistException, AccountValidationException, IOException {
        String accessToken = googleService.getToken(code);
        GooglePojo googlePojo = googleService.getUserInfo(accessToken);

        Account account = getByEmail(googlePojo.getEmail());

        if(account == null) {
            account = new Account();
            account.setEmail(googlePojo.getEmail());

            if(googlePojo.getName() == null || googlePojo.getName().equals(""))
                account.setFirstName(account.getEmail().substring(0, account.getEmail().indexOf("@novahub.vn")));
            else {
                account.setFirstName(googlePojo.getGiven_name());
                account.setLastName(googlePojo.getFamily_name());
            }
            account.setAvatarUrl(googlePojo.getPicture());
            account.setToken(accessToken);
            account.setRoleId(roleService.getByName(RoleEnum.USER.name()).getId());
            account.setUpdatedAt(new Date());
            account.setCreatedAt(new Date());
            account = createWithGoogleAccount(account);

            account.setRole(roleService.getById(account.getRoleId()));
        } else {
            account.setToken(accessToken);
            account = updateToken(account, accessToken);
        }

        UserDetails userDetail = googleService.buildUser(googlePojo, "ROLE_" + account.getRole().getName());
        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetail, null,
                userDetail.getAuthorities());
        authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        SecurityContextHolder.getContext().setAuthentication(authentication);

        return account;
    }

    @Override
    public Page<Account> getAll(String keyword, String status, String role, Pageable pageable) {
        keyword = "%" + keyword + "%";

        if(status.equals("") && role.equals(""))
            return accountRepository.getAllByEmailLikeOrFirstNameLikeOrLastNameLike(keyword, pageable);

        if(!status.equals("") && role.equals(""))
            return accountRepository.getAllByEmailLikeOrFirstNameLikeOrLastNameLikeAndStatus(keyword, status, pageable);

        if(status.equals("") && !role.equals(""))
            return accountRepository.getAllByEmailLikeOrFirstNameLikeOrLastNameLikeAndRole(keyword, role, pageable);

        //if status != "" and role != ""
        return accountRepository.getAllByEmailLikeOrFirstNameLikeOrLastNameLikeAndStatusAndRole(keyword, status, role, pageable);
    }

    @Override
    public Account get(long accountId) throws AccountNotFoundException {

        Account account = accountRepository.getById(accountId);

        if (account == null)
            throw new AccountNotFoundException(accountId);

        return account;
    }

    @Override
    public Account create(Account account) throws AccountIsExistException, RoleNotFoundException, AccountValidationException, MessagingException, IOException {

        accountValidation.validate(account, GroupCreateAccount.class);

        if(accountRepository.getByEmail(account.getEmail()) != null)
            throw new AccountIsExistException(account.getEmail());

        account.setPassword(bCryptPasswordEncoder.encode(account.getPassword()));
        account.setStatus(AccountEnum.INACTIVE.name());
        account.setVertificationToken(tokenService.generateToken(account.getEmail() + account.getEmail()));
        account.setRoleId(roleService.getByName(RoleEnum.USER.name()).getId());
        account.setCreatedAt(new Date());
        account.setUpdatedAt(new Date());

        account = accountRepository.save(account);

        Mail mail = new Mail();
        mail.setEmailReceiving(new String[]{account.getEmail()});
        mail.setSubject(env.getProperty("subject_email_sign_up"));
        String urlAccountActive = "http://localhost:8080/api/users/" + account.getId() + "/active?token=" + account.getVertificationToken();
        String contentEmailSignUp = mailService.getContentMail("sign_up.html");
        contentEmailSignUp = contentEmailSignUp.replace("{url-activate-account}", urlAccountActive);
        mail.setContent(contentEmailSignUp);
        mailService.sendHTMLMail(mail);

        return account;
    }

    @Override
    public Account createWithGoogleAccount(Account account) throws AccountValidationException, AccountIsExistException, RoleNotFoundException {

        accountValidation.validate(account, GroupCreateWithAccountGoogle.class);

        if(accountRepository.getByEmail(account.getEmail()) != null)
            throw new AccountIsExistException(account.getEmail());

        account.setStatus(AccountEnum.ACTIVE.name());
        account.setRoleId(roleService.getByName(RoleEnum.USER.name()).getId());
        account.setPassword(null);
        account.setVertificationToken(null);
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
                accountValidation.validate(account, GroupUpdatePasswordAccount.class);

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

        accountValidation.validate(oldAccount, Default.class);

        return accountRepository.save(oldAccount);
    }

    @Override
    public Account updatedForAdmin(long accountId, Account account) throws AccountValidationException {
        Account oldAccount = accountRepository.getById(accountId);

        // check changing password
        if(account.getPassword() != null){
            accountValidation.validate(account, GroupUpdatePasswordAccount.class);
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
        if(account.getStatus() != null) {
            if(oldAccount.getStatus().equals(AccountEnum.INACTIVE.name())
                    && account.getStatus().equals(AccountEnum.ACTIVE.name()))
                oldAccount.setVertificationToken(null);

            oldAccount.setStatus(account.getStatus());
        }
        if(account.getJoiningDate() != null)
            oldAccount.setJoiningDate(account.getJoiningDate());

        accountValidation.validate(oldAccount, Default.class);

        oldAccount.setUpdatedAt(new Date());
        return accountRepository.save(oldAccount);
    }

    @Override
    public void delete(long accountId) throws AccountNotFoundException {

        if(accountRepository.getById(accountId) == null)
            throw new AccountNotFoundException(accountId);

        accountRepository.deleteById(accountId);
    }

}
