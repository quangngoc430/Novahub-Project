package vn.novahub.helpdesk.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import vn.novahub.helpdesk.exception.CategoryIsExistException;
import vn.novahub.helpdesk.exception.CategoryNotFoundException;
import vn.novahub.helpdesk.exception.CategoryValidationException;
import vn.novahub.helpdesk.model.Category;
import vn.novahub.helpdesk.repository.CategoryRepository;
import vn.novahub.helpdesk.validation.CategoryValidation;

import javax.validation.groups.Default;
@Service
public class CategoryServiceImpl implements CategoryService{

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private CategoryValidation categoryValidation;

    @Override
    public Page<Category> getAllByName(String name,
                                       Pageable pageable) {
        return categoryRepository.getAllByNameLike("%" + name + "%", pageable);
    }

    @Override
    public Category get(long categoryId) throws CategoryNotFoundException {
        Category category = categoryRepository.getById(categoryId);

        if(category == null)
            throw new CategoryNotFoundException(categoryId);

        return category;
    }

    @Override
    public Category create(Category category) throws CategoryIsExistException, CategoryValidationException {
        categoryValidation.validate(category, Default.class);

        if(categoryRepository.existsByName(category.getName()))
            throw new CategoryIsExistException(category.getName());

        return categoryRepository.save(category);
    }

    @Override
    public Category update(Category category,
                           long categoryId) throws CategoryValidationException, CategoryIsExistException {
        categoryValidation.validate(category, Default.class);

        Category oldCategory = categoryRepository.getById(categoryId);

        if(!oldCategory.getName().equals(category.getName()) && categoryRepository.existsByName(category.getName()))
            throw new CategoryIsExistException(category.getName());

        oldCategory.setName(category.getName());

        return categoryRepository.save(oldCategory);
    }

    @Override
    public void delete(long categoryId) throws CategoryNotFoundException {

        if(!categoryRepository.existsById(categoryId))
            throw new CategoryNotFoundException(categoryId);

        categoryRepository.deleteById(categoryId);
    }
}
