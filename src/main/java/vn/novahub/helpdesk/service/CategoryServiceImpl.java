package vn.novahub.helpdesk.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import vn.novahub.helpdesk.exception.CategoryNotFoundException;
import vn.novahub.helpdesk.model.Category;
import vn.novahub.helpdesk.repository.CategoryRepository;
@Service
public class CategoryServiceImpl implements CategoryService{

    @Autowired
    private CategoryRepository categoryRepository;

    @Override
    public Page<Category> getAllByName(String name,
                                       Pageable pageable) {
        name = "%" + name + "%";
        return categoryRepository.getAllByNameLike(name, pageable);
    }

    @Override
    public Category get(long categoryId) throws CategoryNotFoundException {
        Category category = categoryRepository.getById(categoryId);

        if(category == null)
            throw new CategoryNotFoundException(categoryId);

        return category;
    }

    @Override
    public Category create(Category category) {
        return categoryRepository.save(category);
    }

    @Override
    public Category update(Category category,
                           long categoryId) throws CategoryNotFoundException {
        Category oldCategory = categoryRepository.getById(categoryId);

        if(oldCategory == null)
            throw new CategoryNotFoundException(categoryId);

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
