package vn.novahub.helpdesk.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import vn.novahub.helpdesk.exception.IssueIsClosedException;
import vn.novahub.helpdesk.exception.IssueNotFoundException;
import vn.novahub.helpdesk.exception.IssueValidationException;
import vn.novahub.helpdesk.model.Issue;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;

public interface IssueService {

    boolean isIssueOfAccountLogin(long issueId);

    Page<Issue> getAllByKeywordAndStatusForAdmin(String keyword, String status, Pageable pageable);

    Issue getByIdForAdmin(long issueId) throws IssueNotFoundException;

    Issue updateForAdmin(long issuetId, Issue issue) throws IssueNotFoundException, IssueValidationException, MessagingException;

    void deleteForAdmin(long issueId) throws IssueNotFoundException;

    Page<Issue> getAllByKeywordAndStatus(String keyword, String status, Pageable pageable);

    Issue getById(long issueId) throws IssueNotFoundException;

    Issue create(Issue issue) throws IssueValidationException, MessagingException;

    Issue update(long issueId, Issue issue) throws IssueNotFoundException, IssueValidationException, MessagingException;

    void delete(long issueId) throws IssueNotFoundException;


    void approve(long issueId, String token) throws IssueNotFoundException, IssueIsClosedException, MessagingException;

    void deny(long issueId, String token) throws IssueNotFoundException, IssueIsClosedException, MessagingException;
}
