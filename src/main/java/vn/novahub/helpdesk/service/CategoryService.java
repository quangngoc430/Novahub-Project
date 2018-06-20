package vn.novahub.helpdesk.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import vn.novahub.helpdesk.exception.CategoryNotFoundException;
import vn.novahub.helpdesk.model.Category;
import vn.novahub.helpdesk.model.Skill;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;

public interface CategoryService {

    Category create(Category category, HttpServletRequest request);

    Category update(Category category, long categoryId, HttpServletRequest request) throws CategoryNotFoundException;

    Category get(long categoryId, HttpServletRequest request) throws CategoryNotFoundException;

    Page<Category> getAllByName(String name, Pageable pageable, HttpServletRequest request);

    void delete(long categoryId, HttpServletRequest request) throws CategoryNotFoundException;
}
