package vn.novahub.helpdesk.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import vn.novahub.helpdesk.enums.RoleEnum;

import javax.annotation.security.PermitAll;
import javax.servlet.http.HttpServletRequest;

@Controller
public class HomeController {

    private static String PREFIX = "ROLE_";

    @PermitAll
    @RequestMapping("/login")
    public String login() {

        String roleName = (SecurityContextHolder.getContext().getAuthentication().getAuthorities().toArray())[0].toString();

        if(roleName.equals(PREFIX + RoleEnum.ADMIN.name()))
            return "redirect:/admin";
        else if(roleName.equals(PREFIX + RoleEnum.CLERK.name()))
            return "redirect:/clerk";
        else if(roleName.equals(PREFIX + RoleEnum.USER.name()))
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
    public String user() {
        return "user";
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @RequestMapping("/admin")
    public String admin() {
        return "admin";
    }

    @PreAuthorize("hasRole('ROLE_CLERK')")
    @RequestMapping("/clerk")
    public String clerk() {
        return "clerk";
    }

    @RequestMapping("/403")
    public String error(){

        return "403";
    }

    @RequestMapping("/index")
    public String index() {
        return "index";

    }
}