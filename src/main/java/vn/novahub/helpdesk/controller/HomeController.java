package vn.novahub.helpdesk.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
public class HomeController {

    @RequestMapping("/swagger/openapi.json")
    public String swagger() {
        return "../swagger/example/openapi.json";
    }
}