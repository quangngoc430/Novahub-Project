package vn.novahub.helpdesk.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import vn.novahub.helpdesk.exception.CategoryIsExistException;
import vn.novahub.helpdesk.exception.CategoryNotFoundException;
import vn.novahub.helpdesk.exception.CategoryValidationException;
import vn.novahub.helpdesk.model.Category;
import vn.novahub.helpdesk.repository.CategoryRepository;
import vn.novahub.helpdesk.service.CategoryService;
import vn.novahub.helpdesk.validation.CategoryValidation;
import vn.novahub.helpdesk.validation.GroupCreateCategory;
import vn.novahub.helpdesk.validation.GroupUpdateCategory;

import java.util.Date;
import java.util.Optional;

@Service
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private CategoryValidation categoryValidation;

    @Override
    public Page<Category> getAllByName(String name,
                                       Pageable pageable) {
        return categoryRepository.getAllByNameContaining(name, pageable);
    }

    @Override
    public Category get(long categoryId) throws CategoryNotFoundException {
        Optional<Category> categoryOptional = categoryRepository.findById(categoryId);

        if(!categoryOptional.isPresent())
            throw new CategoryNotFoundException(categoryId);

        return categoryOptional.get();
    }

    @Override
    public Category create(Category category) throws CategoryIsExistException, CategoryValidationException {

        categoryValidation.validate(category, GroupCreateCategory.class);

        if(categoryRepository.existsByName(category.getName()))
            throw new CategoryIsExistException(category.getName());

        category.setCreatedAt(new Date());
        category.setUpdatedAt(new Date());

        return categoryRepository.save(category);
    }

    @Override
    public Category update(Category category,
                           long categoryId) throws CategoryValidationException, CategoryIsExistException, CategoryNotFoundException {

        categoryValidation.validate(category, GroupUpdateCategory.class);

        Optional<Category> categoryOptional = categoryRepository.findById(categoryId);

        if(!categoryOptional.isPresent())
            throw new CategoryNotFoundException(categoryId);

        Category oldCategory = categoryOptional.get();

        if(!oldCategory.equals(category) && categoryRepository.existsByName(category.getName()))
            throw new CategoryIsExistException(category.getName());

        oldCategory.setName(category.getName());
        oldCategory.setUpdatedAt(new Date());

        return categoryRepository.save(oldCategory);
    }

    @Override
    public void delete(long categoryId) throws CategoryNotFoundException {

        if(!categoryRepository.existsById(categoryId))
            throw new CategoryNotFoundException(categoryId);

        categoryRepository.deleteById(categoryId);
    }
}