package vn.novahub.helpdesk.service;

import vn.novahub.helpdesk.model.Category;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;

public interface CategoryService {

    Category createCategory(Category category, HttpServletRequest request);

    Category updateCategory(Category category, long categoryId, HttpServletRequest request);

    Category getCategoryByCategoryId(long categoryId, HttpServletRequest request);

    ArrayList<Category> getAllCategories(String name, HttpServletRequest request);

    void deleteCategoryByCategoryId(long categoryId, HttpServletRequest request);
}
