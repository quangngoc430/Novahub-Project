package vn.novahub.helpdesk.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import vn.novahub.helpdesk.exception.IssueIsClosedException;
import vn.novahub.helpdesk.exception.IssueNotFoundException;
import vn.novahub.helpdesk.exception.IssueValidationException;
import vn.novahub.helpdesk.model.Issue;

import javax.mail.MessagingException;
import java.io.IOException;

public interface AccountIssueService {

    Page<Issue> getAllByKeywordAndStatus(String keyword, String status, Pageable pageable);

    Issue findOne(long issueId) throws IssueNotFoundException;

    Issue create(Issue issue) throws IssueValidationException, MessagingException, IOException;

    Issue update(long issueId, Issue issue) throws IssueNotFoundException, IssueValidationException, MessagingException, IOException, IssueIsClosedException;

    void delete(long issueId) throws IssueNotFoundException;
}
