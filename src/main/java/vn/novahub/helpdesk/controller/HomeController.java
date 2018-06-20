package vn.novahub.helpdesk.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import vn.novahub.helpdesk.model.Mail;
import vn.novahub.helpdesk.service.AccountService;
import vn.novahub.helpdesk.service.LogService;
import vn.novahub.helpdesk.service.MailService;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;

@RestController
public class HomeController {

    @Autowired
    private AccountService accountService;


    @RequestMapping("/")
    public String index() throws MessagingException {
        return "Greetings from Spring Boot!";
    }

    @RequestMapping("/login")
    public ModelAndView login(){
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("login");

        return  modelAndView;
    }

    @GetMapping(value = "/api/login", produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<?> loginAccount(@RequestHeader("email") String email,
                                       @RequestHeader("password") String password,
                                       HttpServletRequest request){
        if(accountService.loginAccount(email, password, request))
            return new ResponseEntity<>("success", HttpStatus.OK);
        else
            return new ResponseEntity<>("fail", HttpStatus.OK);
    }
    
}