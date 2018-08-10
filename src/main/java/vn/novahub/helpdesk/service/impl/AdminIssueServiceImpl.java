package vn.novahub.helpdesk.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import vn.novahub.helpdesk.enums.IssueEnum;
import vn.novahub.helpdesk.exception.IssueIsClosedException;
import vn.novahub.helpdesk.exception.IssueNotFoundException;
import vn.novahub.helpdesk.exception.IssueValidationException;
import vn.novahub.helpdesk.model.Issue;
import vn.novahub.helpdesk.repository.IssueRepository;
import vn.novahub.helpdesk.service.AdminIssueService;
import vn.novahub.helpdesk.service.MailService;
import vn.novahub.helpdesk.validation.IssueValidation;

import javax.mail.MessagingException;
import javax.validation.groups.Default;
import java.io.IOException;
import java.util.Date;
import java.util.Optional;

@Service
@PropertySource("classpath:email.properties")
public class AdminIssueServiceImpl implements AdminIssueService {

    @Autowired
    private Environment env;

    @Autowired
    private IssueRepository issueRepository;

    @Autowired
    private IssueValidation issueValidation;

    @Autowired
    private MailService mailService;

    @Override
    public Issue findOne(long issueId) throws IssueNotFoundException {
        Optional<Issue> issueOptional = issueRepository.findById(issueId);

        if(!issueOptional.isPresent())
            throw new IssueNotFoundException(issueId);

        return issueOptional.get();
    }

    @Override
    public Page<Issue> getAllByKeywordAndStatus(String keyword, String status, Pageable pageable) {
        if (status.equals(""))
            return issueRepository.getAllByTitleContainingOrContentContaining(keyword, keyword, pageable);

        return issueRepository.getAllByTitleContainingOrContentContainingAndStatus(keyword, keyword, status, pageable);
    }

    @Override
    public Issue update(long issueId, Issue issue) throws IssueNotFoundException, IssueValidationException, MessagingException, IOException {
        Optional<Issue> issueOptional = issueRepository.findById(issueId);

        if (!issueOptional.isPresent())
            throw new IssueNotFoundException(issueId);

        Issue oldIssue = issueOptional.get();

        if(issue.getTitle() != null && !issue.getTitle().isEmpty()) {
            oldIssue.setTitle(issue.getTitle());
        }

        if(issue.getContent() != null && !issue.getTitle().isEmpty()) {
            oldIssue.setContent(issue.getContent());
        }

        if (issue.getReplyMessage() != null && !issue.getTitle().isEmpty()) {
            oldIssue.setReplyMessage(issue.getReplyMessage());
        }

        issueValidation.validate(oldIssue, Default.class);
        oldIssue.setUpdatedAt(new Date());
        oldIssue = issueRepository.save(oldIssue);

        return oldIssue;
    }

    public void delete(long issueId) throws IssueNotFoundException{
        Optional<Issue> issueOptional = issueRepository.findById(issueId);

        if (!issueOptional.isPresent())
            throw new IssueNotFoundException(issueId);

        Issue issue = issueOptional.get();
        issue.setStatus(IssueEnum.CANCELLED.name());

        issueRepository.save(issue);
    }

    @Override
    public void approve(long issueId) throws IssueNotFoundException, IssueIsClosedException, MessagingException, IOException {
        Issue issue = issueRepository.getByIdAndStatusIsNot(issueId, IssueEnum.CANCELLED.name());

        if (issue == null)
            throw new IssueNotFoundException(issueId);

        if (issue.getToken() == null || issue.getStatus().equals(IssueEnum.APPROVE.name()) || issue.getStatus().equals(IssueEnum.DENY.name()))
            throw new IssueIsClosedException(issueId);

        issue.setToken(null);
        issue.setStatus(IssueEnum.APPROVE.name());
        issue = issueRepository.save(issue);

        mailService.sendMailUpdateIssueForUser(issue);
        mailService.sendMailUpdateIssueForClerk(issue);
    }

    @Override
    public void deny(long issueId) throws IssueNotFoundException, IssueIsClosedException, MessagingException, IOException {
        Issue issue = issueRepository.getByIdAndStatusIsNot(issueId, IssueEnum.CANCELLED.name());

        if (issue == null)
            throw new IssueNotFoundException(issueId);

        if (issue.getToken() == null || issue.getStatus().equals(IssueEnum.APPROVE.name()) || issue.getStatus().equals(IssueEnum.DENY.name()))
            throw new IssueIsClosedException(issueId);

        issue.setToken(null);
        issue.setStatus(IssueEnum.DENY.name());
        issue = issueRepository.save(issue);

        mailService.sendMailUpdateIssueForUser(issue);
        mailService.sendMailUpdateIssueForClerk(issue);
    }
}
