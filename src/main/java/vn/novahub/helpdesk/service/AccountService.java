package vn.novahub.helpdesk.service;

import vn.novahub.helpdesk.model.Account;

import javax.servlet.http.HttpServletRequest;

public interface AccountService {

    boolean loginAccount(String email, String password, HttpServletRequest request);

    Account getAccountLogin(HttpServletRequest request);

}
