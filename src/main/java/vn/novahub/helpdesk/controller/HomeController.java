package vn.novahub.helpdesk.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import vn.novahub.helpdesk.constant.RoleConstant;
import vn.novahub.helpdesk.exception.AccountIsExistException;
import vn.novahub.helpdesk.exception.AccountValidationException;
import vn.novahub.helpdesk.exception.EmailFormatException;
import vn.novahub.helpdesk.exception.RoleNotFoundException;
import vn.novahub.helpdesk.model.Account;
import vn.novahub.helpdesk.model.GooglePojo;
import vn.novahub.helpdesk.service.AccountService;
import vn.novahub.helpdesk.service.GoogleService;
import vn.novahub.helpdesk.service.RoleService;

import javax.annotation.security.PermitAll;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Date;

@Controller
public class HomeController {

    @Autowired
    private AccountService accountService;

    @Autowired
    private GoogleService googleService;

    @Autowired
    private RoleService roleService;

    @PermitAll
    @RequestMapping("/login")
    public String login() {

        String roleName = (SecurityContextHolder.getContext().getAuthentication().getAuthorities().toArray())[0].toString();

        if(roleName.equals(RoleConstant.PREFIX_ROLE + RoleConstant.ROLE_ADMIN))
            return "redirect:/admin";
        else if(roleName.equals(RoleConstant.PREFIX_ROLE + RoleConstant.ROLE_CLERK))
            return "redirect:/clerk";
        else if(roleName.equals(RoleConstant.PREFIX_ROLE + RoleConstant.ROLE_USER))
            return "redirect:/user";

        return "login";
    }

    @PreAuthorize("isAuthenticated()")
    @RequestMapping("/update")
    public String updateClerk() {
        return "account_update";
    }

    @RequestMapping("/register")
    public String register(@RequestParam(value = "email", required = false, defaultValue = "") String email,
                           @RequestParam(value = "firstName", required = false, defaultValue = "") String firstName,
                           @RequestParam(value = "lastName", required = false, defaultValue = "") String lastName,
                           HttpServletRequest request){
        request.setAttribute("email", email);
        request.setAttribute("fistName", firstName);
        request.setAttribute("lastName", lastName);
        return "register";
    }

    @PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')")
    @RequestMapping("/user")
    public String user(){
        return "user";
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @RequestMapping("/admin")
    public String admin(){
        return "admin";
    }

    @PreAuthorize("hasRole('ROLE_CLERK')")
    @RequestMapping("/clerk")
    public String clerk(){
        return "clerk";
    }

    @RequestMapping("/403")
    public String er(){
        return "403";
    }

    @RequestMapping("/login-google")
    public String loginGoogle(@RequestParam(value = "code", defaultValue = "") String code,
                              HttpServletRequest request) throws IOException, EmailFormatException, AccountIsExistException, AccountValidationException, RoleNotFoundException {
        if (code.isEmpty())
            return "redirect:/login?google=error";

        String accessToken = googleService.getToken(code);
        GooglePojo googlePojo = googleService.getUserInfo(accessToken);

        Account account = accountService.getByEmail(googlePojo.getEmail());

        if(account == null) {
            account = new Account();
            account.setEmail(googlePojo.getEmail());
            account.setFirstName(googlePojo.getGiven_name());
            account.setLastName(googlePojo.getFamily_name());
            account.setAvatarUrl(googlePojo.getPicture());
            account.setToken(accessToken);
            account.setRoleId(roleService.getByName(RoleConstant.ROLE_USER).getId());
            account.setUpdatedAt(new Date());
            account.setCreatedAt(new Date());
            account = accountService.createWithGoogleAccount(account);
            account.setRole(roleService.getById(account.getRoleId()));
        } else {
            account.setToken(accessToken);
            account = accountService.updateToken(account, accessToken);
        }

        UserDetails userDetail = googleService.buildUser(googlePojo, RoleConstant.PREFIX_ROLE + account.getRole().getName());
        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetail, null,
                userDetail.getAuthorities());
        authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        SecurityContextHolder.getContext().setAuthentication(authentication);

        if(account.getRole().getName().equals(RoleConstant.ROLE_ADMIN))
            return "redirect:/admin";
        else if(account.getRole().getName().equals(RoleConstant.ROLE_CLERK))
            return "redirect:/clerk";

        return "redirect:/user";
    }

}