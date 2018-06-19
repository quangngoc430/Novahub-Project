package vn.novahub.helpdesk.service;

import vn.novahub.helpdesk.model.Category;
import vn.novahub.helpdesk.model.Skill;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;

public interface CategoryService {

    Category createACategory(Category category, HttpServletRequest request);

    Category updateACategory(Category category, long categoryId, HttpServletRequest request);

    Category getACategory(long categoryId, HttpServletRequest request);

    ArrayList<Category> getAllCategories(String name, HttpServletRequest request);

    void deleteACategory(long categoryId, HttpServletRequest request);
}
