package vn.novahub.helpdesk.exception;

public class IssueIsClosedException extends Exception {

    public IssueIsClosedException(long issueId){
        super("IssueIsClosedException with issueId = " + issueId);
    }
}
