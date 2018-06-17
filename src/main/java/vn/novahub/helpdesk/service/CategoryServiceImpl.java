package vn.novahub.helpdesk.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import vn.novahub.helpdesk.model.Category;
import vn.novahub.helpdesk.repository.CategoryRepository;

import java.util.ArrayList;

@Service
public class CategoryServiceImpl implements CategoryService{

    @Autowired
    private CategoryRepository categoryRepository;

    @Override
    public Category createCategory(Category category) {
        return categoryRepository.save(category);
    }

    @Override
    public Category updateCategory(Category category, long categoryId) {
        // TODO: check category is exist

        Category oldCategory = categoryRepository.findById(categoryId).get();
        oldCategory.setName(category.getName().toLowerCase());
        Category newCategory = categoryRepository.save(oldCategory);

        // TODO: delete category if it don't belong to any skills

        return newCategory;
    }

    @Override
    public Category getCategoryByCategoryId(long categoryId) {
        return categoryRepository.findById(categoryId).get();
    }

    @Override
    public ArrayList<Category> getAllCategories(String name) {
        return (ArrayList<Category>) categoryRepository.getAllByNameLike("%" + name.toLowerCase() + "%");
    }

    @Override
    public void deleteCategoryByCategoryId(long categoryId) {
        categoryRepository.deleteById(categoryId);
    }
}
