package vn.novahub.helpdesk.validation;

import vn.novahub.helpdesk.exception.CategoryValidationException;
import vn.novahub.helpdesk.model.Category;

public interface CategoryValidation {

    void validate(Category category, Class classGroupValidation) throws CategoryValidationException;
}
