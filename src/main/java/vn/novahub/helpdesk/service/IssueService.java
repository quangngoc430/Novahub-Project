package vn.novahub.helpdesk.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import vn.novahub.helpdesk.exception.IssueNotFoundException;
import vn.novahub.helpdesk.model.Issue;

import javax.servlet.http.HttpServletRequest;

public interface IssueService {

    Issue getByIssueId(long issueId) throws IssueNotFoundException;

    Issue getOfAccountByIssueIdAndAccountId(long issueId, HttpServletRequest request) throws IssueNotFoundException;

    Page<Issue> getAllByKeyword(String keyword, String status, Pageable pageable);

    Page<Issue> getAllOfAccountByKeyword(String keyword, String status, Pageable pageable, HttpServletRequest request);

    Issue create(Issue issue, HttpServletRequest request);

    Issue update(long issuetId, Issue issue) throws IssueNotFoundException;

    void delete(long issueId) throws IssueNotFoundException;

    boolean approve(long issueId, String token) throws IssueNotFoundException;

    boolean deny(long issueId, String token) throws IssueNotFoundException;
}
