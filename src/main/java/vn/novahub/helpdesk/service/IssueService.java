package vn.novahub.helpdesk.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import vn.novahub.helpdesk.exception.IssueNotFoundException;
import vn.novahub.helpdesk.model.Issue;

import javax.servlet.http.HttpServletRequest;

public interface IssueService {

    Issue getByIssueId(long issueId, HttpServletRequest request) throws IssueNotFoundException;

    Issue getOfAccountByIssueIdAndAccountId(long issueId, HttpServletRequest request) throws IssueNotFoundException;

    Page<Issue> getAllByKeyword(String keyword, String status, Pageable pageable, HttpServletRequest request);

    Page<Issue> getAllOfAccountByKeyword(String keyword, String status, Pageable pageable, HttpServletRequest request);

    Issue create(Issue issue, HttpServletRequest request);

    Issue update(long issuetId, Issue issue, HttpServletRequest request) throws IssueNotFoundException;

    void delete(long issueId, HttpServletRequest request) throws IssueNotFoundException;

    boolean approve(long issueId, String token, HttpServletRequest request) throws IssueNotFoundException;

    boolean deny(long issueId, String token, HttpServletRequest request) throws IssueNotFoundException;
}
