package vn.novahub.helpdesk.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import vn.novahub.helpdesk.exception.IssueNotFoundException;
import vn.novahub.helpdesk.exception.IssueValidationException;
import vn.novahub.helpdesk.model.Issue;

import javax.mail.MessagingException;

public interface AccountIssueService {

    Page<Issue> getAllByKeywordAndStatus(String keyword, String status, Pageable pageable);

    Issue findOne(long issueId) throws IssueNotFoundException;

    Issue create(Issue issue) throws IssueValidationException, MessagingException;

    Issue update(long issueId, Issue issue) throws IssueNotFoundException, IssueValidationException, MessagingException;

    void delete(long issueId) throws IssueNotFoundException;
}
