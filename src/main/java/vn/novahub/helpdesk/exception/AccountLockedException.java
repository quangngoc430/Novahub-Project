package vn.novahub.helpdesk.exception;

public class AccountLockedException extends Exception{

    public AccountLockedException(long accountId){
        super("AccountLockedException with accountId = " + accountId);
    }

    public AccountLockedException(String email) {
        super("AccountLockedException with email = " + email);
    }
}
