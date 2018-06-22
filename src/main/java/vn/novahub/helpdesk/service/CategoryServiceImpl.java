package vn.novahub.helpdesk.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import vn.novahub.helpdesk.exception.CategoryNotFoundException;
import vn.novahub.helpdesk.model.Category;
import vn.novahub.helpdesk.repository.CategoryRepository;
import vn.novahub.helpdesk.repository.SkillRepository;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;

@Service
public class CategoryServiceImpl implements CategoryService{

    @Autowired
    private CategoryRepository categoryRepository;

    @Override
    public Page<Category> getAllByName(String name,
                                       Pageable pageable,
                                       HttpServletRequest request) {
        name = "%" + name + "%";
        return categoryRepository.getAllByNameLike(name, pageable);
    }

    @Override
    public Category get(long categoryId,
                        HttpServletRequest request) throws CategoryNotFoundException {
        Category category = categoryRepository.getById(categoryId);

        if(category == null)
            throw new CategoryNotFoundException(categoryId);

        return category;
    }

    @Override
    public Category create(Category category,
                           HttpServletRequest request) {
        return categoryRepository.save(category);
    }

    @Override
    public Category update(Category category,
                           long categoryId,
                           HttpServletRequest request) throws CategoryNotFoundException {
        Category oldCategory = categoryRepository.getById(categoryId);

        if(oldCategory == null)
            throw new CategoryNotFoundException(categoryId);

        oldCategory.setName(category.getName());
        Category newCategory = categoryRepository.save(oldCategory);

        return newCategory;
    }

    @Override
    public void delete(long categoryId,
                       HttpServletRequest request) throws CategoryNotFoundException {

        if(!categoryRepository.existsById(categoryId))
            throw new CategoryNotFoundException(categoryId);

        categoryRepository.deleteById(categoryId);
    }
}
