package vn.novahub.helpdesk.service;

import vn.novahub.helpdesk.model.Category;

import java.util.ArrayList;

public interface CategoryService {

    Category createCategory(Category category);

    Category updateCategory(Category category, long categoryId);

    Category getCategoryByCategoryId(long categoryId);

    ArrayList<Category> getAllCategories(String name);

    void deleteCategoryByCategoryId(long categoryId);
}
