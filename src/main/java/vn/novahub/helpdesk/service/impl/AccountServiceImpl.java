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
import vn.novahub.helpdesk.enums.TokenEnum;
import vn.novahub.helpdesk.exception.*;
import vn.novahub.helpdesk.model.*;
import vn.novahub.helpdesk.repository.AccountRepository;
import vn.novahub.helpdesk.repository.TokenRepository;
import vn.novahub.helpdesk.service.*;
import vn.novahub.helpdesk.validation.*;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import javax.validation.groups.Default;
import java.io.IOException;
import java.util.Date;
import java.util.Optional;

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
    private TokenRepository tokenRepository;

    @Autowired
    private TokenValidation tokenValidation;

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
    public void authenticationToken(String authenticationToken, HttpServletRequest request) throws TokenIsExpiredException, UnauthorizedException {
        Token token = tokenRepository.getByAccessToken(authenticationToken);

        if(token == null) {
            throw new UnauthorizedException("Invalid token", request.getAttribute("url_request").toString());
        }

        if(tokenService.isTokenExpired(token)) {
            throw new TokenIsExpiredException(token.getAccessToken());
        }

        Account accountLogin = token.getAccount();
        UserDetails userDetails = new User(accountLogin.getEmail(), "", accountLogin.getAuthorities());
        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
        authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    @Override
    public Account getAccountLogin() {
        String email = ((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername();
        return accountRepository.getByEmail(email);
    }

    @Override
    public Account getByEmail(String email) {
        return accountRepository.getByEmail(email);
    }

    @Override
    public boolean activateAccount(long accountId, String verificationToken) {
        Account account = accountRepository.getByIdAndVerificationToken(accountId, verificationToken);

        if(account == null) {
            return false;
        }

        account.setStatus(AccountEnum.ACTIVE.name());
        account.setVerificationToken(null);
        accountRepository.save(account);

        return true;
    }

    @Override
    public Token login(Account accountInput) throws AccountInvalidException, AccountInactiveException, AccountLockedException, AccountValidationException {

        accountValidation.validate(accountInput, GroupLoginAccount.class);

        Account account = accountRepository.getByEmail(accountInput.getEmail());

        if(account == null || account.getPassword() == null || !bCryptPasswordEncoder.matches(accountInput.getPassword(), account.getPassword()))
            throw new AccountInvalidException();

        if(account.getStatus().equals(AccountEnum.INACTIVE.name()))
            throw new AccountInactiveException(account.getEmail());

        if(account.getStatus().equals(AccountEnum.LOCKED.name()))
            throw new AccountLockedException(account.getEmail());

        Token accessToken = new Token();
        accessToken.setAccessToken(tokenService.generateToken(account.getId() + account.getEmail() + (new Date()).getTime()));
        accessToken.setExpiredIn(TokenEnum.TIME_OF_TOKEN.value());
        accessToken.setExpiredAt(new Date((new Date()).getTime() + TokenEnum.TIME_OF_TOKEN.value() * 1000));
        accessToken.setAccountId(account.getId());
        accessToken.setCreatedAt(new Date());
        accessToken.setUpdatedAt(new Date());
        accessToken = tokenRepository.save(accessToken);
        accessToken.setAccount(account);

        return accessToken;
    }

    @Override
    public Token loginWithGoogle(Token token) throws IOException, EmailFormatException, RoleNotFoundException, UnauthorizedException, TokenIsExpiredException, AccountValidationException {

        tokenValidation.validate(token, GroupLoginWithGoogle.class);

        GooglePojo googlePojo = googleService.getUserInfo(token.getAccessToken());

        Account account = getByEmail(googlePojo.getEmail());

        if(account == null) {
            account = new Account();
            account.setEmail(googlePojo.getEmail());
            account.setFirstName(googlePojo.getGivenName());
            account.setLastName(googlePojo.getFamilyName());
            account.setAvatarUrl(googlePojo.getPicture());
            account.setVerificationToken(null);
            account.setPassword(null);
            account.setStatus(AccountEnum.ACTIVE.name());
            Role role = roleService.getByName(RoleEnum.USER.name());
            account.setRoleId(role.getId());
            account.setCreatedAt(new Date());
            account.setUpdatedAt(new Date());

            account = accountRepository.save(account);
            account.setRole(role);
        } else {
            if(account.getVerificationToken() != null) {
                account.setVerificationToken(null);
                accountRepository.save(account);
            }
        }

        Token accessToken = tokenRepository.getByAccessToken(token.getAccessToken());
        if(accessToken == null) {
            accessToken = new Token();
            accessToken.setAccessToken(token.getAccessToken());
            accessToken.setExpiredIn(TokenEnum.TIME_OF_TOKEN.value());
            accessToken.setExpiredAt((new Date((new Date()).getTime() + TokenEnum.TIME_OF_TOKEN.value() * 1000)));
            accessToken.setAccountId(account.getId());
            accessToken.setCreatedAt(new Date());
            accessToken.setUpdatedAt(new Date());
            accessToken = tokenRepository.save(accessToken);
            accessToken.setAccount(account);
        } else {
            if(tokenService.isTokenExpired(accessToken)) {
                throw new TokenIsExpiredException(token.getAccessToken());
            }
        }

        return accessToken;
    }

    @Override
    public void logout(String accessToken) throws TokenNotFoundException {
        Token token = tokenRepository.getByAccessToken(accessToken);

        if(token == null)
            throw new TokenNotFoundException(accessToken);

        tokenRepository.save(token);
    }

    @Override
    public Page<Account> getAll(String keyword, String status, String role, Pageable pageable) {
        if(status.equals("") && role.equals(""))
            return accountRepository.getAllByEmailContainingOrFirstNameContainingOrLastNameContaining(keyword, pageable);

        if(!status.equals("") && role.equals(""))
            return accountRepository.getAllByEmailContainingOrFirstNameContainingOrLastNameContainingAndStatus(keyword, status, pageable);

        if(status.equals("") && !role.equals(""))
            return accountRepository.getAllByEmailContainingOrFirstNameContainingOrLastNameContainingAndRole(keyword, role, pageable);

        //if status != "" and role != ""
        return accountRepository.getAllByEmailContainingOrFirstNameContainingOrLastNameContainingAndStatusAndRole(keyword, status, role, pageable);
    }

    @Override
    public Account get(long accountId) throws AccountNotFoundException {

        Optional<Account> accountOptional = accountRepository.findById(accountId);

        if(!accountOptional.isPresent())
            throw new AccountNotFoundException(accountId);

        return accountOptional.get();
    }

    @Override
    public Account create(Account account) throws AccountIsExistException, RoleNotFoundException, AccountValidationException, MessagingException, IOException {

        accountValidation.validate(account, GroupCreateAccount.class);

        if(accountRepository.getByEmail(account.getEmail()) != null)
            throw new AccountIsExistException(account.getEmail());

        account.setPassword(bCryptPasswordEncoder.encode(account.getPassword()));
        account.setStatus(AccountEnum.INACTIVE.name());
        account.setVerificationToken(tokenService.generateToken(account.getEmail() + account.getEmail()));
        account.setRoleId(roleService.getByName(RoleEnum.USER.name()).getId());
        account.setCreatedAt(new Date());
        account.setUpdatedAt(new Date());

        account = accountRepository.save(account);

        Mail mail = new Mail();
        mail.setEmailReceiving(new String[]{account.getEmail()});
        mail.setSubject(env.getProperty("subject_email_sign_up"));
        String urlAccountActive = "http://localhost:8080/api/users/" + account.getId() + "/active?token=" + account.getVerificationToken();
        String contentEmailSignUp = mailService.getContentMail("sign_up.html");
        contentEmailSignUp = contentEmailSignUp.replace("{url-activate-account}", urlAccountActive);
        mail.setContent(contentEmailSignUp);
        mailService.sendHTMLMail(mail);

        return account;
    }

    @Override
    public Account update(Account account) throws AccountValidationException, AccountPasswordNotEqualException {

        Account oldAccount = getAccountLogin();

        // check changing password
        if(oldAccount.getPassword() != null){
            if(account.getNewPassword() != null || account.getPassword() != null){
                accountValidation.validate(account, GroupUpdatePasswordByAccount.class);

                if(!bCryptPasswordEncoder.matches(account.getPassword(), oldAccount.getPassword()))
                    throw new AccountPasswordNotEqualException("Password do not match");

                oldAccount.setPassword(bCryptPasswordEncoder.encode(account.getNewPassword()));
            }
        } else {
            if (account.getPassword() != null) {
                accountValidation.validate(account, GroupUpdatePasswordAccountSignupWithGoogle.class);

                oldAccount.setPassword(bCryptPasswordEncoder.encode(account.getPassword()));
            }
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
    public Account updatedForAdmin(long accountId, Account account) throws AccountValidationException, AccountNotFoundException {
        Optional<Account> accountOptional = accountRepository.findById(accountId);

        if(!accountOptional.isPresent())
            throw new AccountNotFoundException(accountId);

        Account oldAccount = accountOptional.get();

        // check changing password
        if(account.getPassword() != null){
            accountValidation.validate(account, GroupUpdatePasswordByAdmin.class);
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
                oldAccount.setVerificationToken(null);

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

        Optional<Account> accountOptional = accountRepository.findById(accountId);

        if(!accountOptional.isPresent())
            throw new AccountNotFoundException(accountId);

        accountRepository.deleteById(accountId);
    }

}