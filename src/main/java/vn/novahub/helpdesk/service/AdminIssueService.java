package vn.novahub.helpdesk.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import vn.novahub.helpdesk.exception.IssueIsClosedException;
import vn.novahub.helpdesk.exception.IssueNotFoundException;
import vn.novahub.helpdesk.exception.IssueValidationException;
import vn.novahub.helpdesk.model.Issue;

import javax.mail.MessagingException;
import java.io.IOException;

public interface AdminIssueService{

    Issue findOne(long issueId) throws IssueNotFoundException;

    Page<Issue> getAllByKeywordAndStatus(String keyword, String status, Pageable pageable);

    Issue update(long issuetId, Issue issue) throws IssueNotFoundException, IssueValidationException, MessagingException, IOException, IssueIsClosedException;

    void delete(long issueId) throws IssueNotFoundException;

    void approve(long issueId, String token) throws IssueNotFoundException, IssueIsClosedException, MessagingException, IOException;

    void deny(long issueId, String token) throws IssueNotFoundException, IssueIsClosedException, MessagingException, IOException;
}
