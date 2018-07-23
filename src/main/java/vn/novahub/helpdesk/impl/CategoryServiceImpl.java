package vn.novahub.helpdesk.impl;

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
import javax.validation.groups.Default;
import java.util.Date;


@Service
public class CategoryServiceImpl implements CategoryService {

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

        if(categoryRepository.existsByName(category.getName()))
            throw new CategoryIsExistException(category.getName());

        category.setCreatedAt(new Date());
        category.setUpdatedAt(new Date());

        categoryValidation.validate(category, Default.class);

        return categoryRepository.save(category);
    }

    @Override
    public Category update(Category category,
                           long categoryId) throws CategoryValidationException, CategoryIsExistException, CategoryNotFoundException {

        Category oldCategory = categoryRepository.getById(categoryId);

        if(oldCategory == null)
            throw new CategoryNotFoundException(categoryId);

        if(!oldCategory.equals(category) && categoryRepository.existsByName(category.getName()))
            throw new CategoryIsExistException(category.getName());

        oldCategory.setName(category.getName());

        oldCategory.setUpdatedAt(new Date());

        categoryValidation.validate(oldCategory, Default.class);


        return categoryRepository.save(oldCategory);
    }

    @Override
    public void delete(long categoryId) throws CategoryNotFoundException {

        if(!categoryRepository.existsById(categoryId))
            throw new CategoryNotFoundException(categoryId);

        categoryRepository.deleteById(categoryId);
    }
}