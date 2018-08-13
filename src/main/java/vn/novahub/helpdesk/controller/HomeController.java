package vn.novahub.helpdesk.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
public class HomeController {

<<<<<<< HEAD
=======
    private static String PREFIX = "ROLE_";
    
    @PermitAll
    @RequestMapping("/login")
    public String login() {

        String roleName = (SecurityContextHolder.getContext().getAuthentication().getAuthorities().toArray())[0].toString();

        if(roleName.equals(PREFIX + RoleEnum.ADMIN.name()))
            return "redirect:/admin";
        else if(roleName.equals(PREFIX + RoleEnum.CLERK.name()))
            return "redirect:/clerk";
        else if(roleName.equals(PREFIX + RoleEnum.EMPLOYEE.name()))
            return "redirect:/user";

        return "login";
    }

>>>>>>> origin/develop
    @RequestMapping("/swagger/openapi.json")
    public String swagger() {
        return "../swagger/example/openapi.json";
    }
}