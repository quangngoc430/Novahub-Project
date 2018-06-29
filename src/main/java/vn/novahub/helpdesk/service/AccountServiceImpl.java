package vn.novahub.helpdesk.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import vn.novahub.helpdesk.model.Account;
import vn.novahub.helpdesk.repository.AccountRepository;

import javax.servlet.http.HttpServletRequest;

@Service
public class AccountServiceImpl implements AccountService {

    @Autowired
    private AccountRepository accountRepository;

    @Override
    public boolean loginAccount(String email, String password, HttpServletRequest request) {
        Account account = accountRepository.findByEmailAndPassword(email, password);

        if(account != null){
            request.getSession().setAttribute("accountLogin", account);
        }

        return account != null;
    }

    @Override
    public Account getAccountLogin(HttpServletRequest request) {
        return (Account) request.getSession().getAttribute("accountLogin");
    }
}
