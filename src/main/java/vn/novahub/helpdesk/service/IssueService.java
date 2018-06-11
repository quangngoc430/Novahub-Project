package vn.novahub.helpdesk.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import vn.novahub.helpdesk.model.Issue;

public interface IssueService {

    Issue getIssueByIssueId(long issueId);

    Issue getIssueOfAccountByIssueIdAndAccountId(long issueId, long accountId);

    Page<Issue> getAllIssuesByKeyword(String keyword, String status, Pageable pageable);

    Page<Issue> getAllIssuesOfAccountByKeyword(long accountId, String keyword, String status, Pageable pageable);

    Issue createIssue(Issue issue);

    Issue updateIssue(long issuetId, Issue issue);

    boolean deleteIssue(long issueId);
}
