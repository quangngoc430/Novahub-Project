package vn.novahub.helpdesk.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import vn.novahub.helpdesk.exception.CategoryIsExistException;
import vn.novahub.helpdesk.exception.CategoryNotFoundException;
import vn.novahub.helpdesk.exception.CategoryValidationException;
import vn.novahub.helpdesk.model.Category;

public interface CategoryService {

    Category create(Category category) throws CategoryIsExistException, CategoryValidationException;

    Category update(Category category, long categoryId) throws CategoryValidationException, CategoryIsExistException, CategoryNotFoundException;

    Category get(long categoryId) throws CategoryNotFoundException;

    Page<Category> getAllByName(String name, Pageable pageable);

    void delete(long categoryId) throws CategoryNotFoundException;
}
