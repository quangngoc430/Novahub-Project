package vn.novahub.helpdesk.validation;

import vn.novahub.helpdesk.exception.IssueValidationException;
import vn.novahub.helpdesk.model.Issue;

public interface IssueValidation {

    void validate(Issue issue, Class classGroupValidation) throws IssueValidationException;
}
