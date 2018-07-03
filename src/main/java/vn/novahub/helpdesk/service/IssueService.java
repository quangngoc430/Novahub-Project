package vn.novahub.helpdesk.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import vn.novahub.helpdesk.exception.IssueIsClosedException;
import vn.novahub.helpdesk.exception.IssueNotFoundException;
import vn.novahub.helpdesk.exception.IssueValidationException;
import vn.novahub.helpdesk.model.Issue;

import javax.servlet.http.HttpServletRequest;

public interface IssueService {

    boolean isIssueOfAccountLogin(long issueId);

    Issue getByIssueId(long issueId) throws IssueNotFoundException;

    Issue getOfAccountLoginByIssueId(long issueId) throws IssueNotFoundException;

    Page<Issue> getAllByKeyword(String keyword, String status, Pageable pageable);

    Page<Issue> getAllOfAccountLoginByKeyword(String keyword, String status, Pageable pageable);

    Issue create(Issue issue) throws IssueValidationException;

    Issue update(long issueId, Issue issue) throws IssueNotFoundException, IssueValidationException;

    Issue updateForAdmin(long issuetId, Issue issue) throws IssueNotFoundException, IssueValidationException;

    void delete(long issueId) throws IssueNotFoundException;

    void deleteForAdmin(long issueId) throws IssueNotFoundException;

    void approve(long issueId, String token) throws IssueNotFoundException, IssueIsClosedException;

    void deny(long issueId, String token) throws IssueNotFoundException, IssueIsClosedException;
}
