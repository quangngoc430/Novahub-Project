package vn.novahub.helpdesk.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import vn.novahub.helpdesk.model.Category;
import vn.novahub.helpdesk.repository.CategoryRepository;
import vn.novahub.helpdesk.repository.SkillRepository;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;

@Service
public class CategoryServiceImpl implements CategoryService{

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private SkillRepository skillRepository;

    @Override
    public ArrayList<Category> getAllCategories(String name,
                                                HttpServletRequest request) {
        return (ArrayList<Category>) categoryRepository.getAllByNameLike("%" + name + "%");
    }

    @Override
    public Category getACategory(long categoryId,
                                 HttpServletRequest request) {
        return categoryRepository.findById(categoryId).get();
    }

    @Override
    public Category createACategory(Category category,
                                   HttpServletRequest request) {
        return categoryRepository.save(category);
    }

    @Override
    public Category updateACategory(Category category,
                                   long categoryId,
                                   HttpServletRequest request) {
        Category oldCategory = categoryRepository.findById(categoryId).get();

        if(oldCategory == null)
            return null;

        oldCategory.setName(category.getName());
        Category newCategory = categoryRepository.save(oldCategory);

        return newCategory;
    }

    @Override
    public void deleteACategory(long categoryId,
                                           HttpServletRequest request) {
        categoryRepository.deleteById(categoryId);
    }
}
