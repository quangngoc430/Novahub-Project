package vn.novahub.helpdesk.seeding;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import vn.novahub.helpdesk.model.Category;
import vn.novahub.helpdesk.repository.CategoryRepository;

import java.util.ArrayList;
import java.util.Date;

@Component
public class CategorySeeding {

    @Autowired
    private CategoryRepository categoryRepository;

    public ArrayList<Category> generateData(ArrayList<String> categoryNames) {

        ArrayList<Category> categoryArrayList = new ArrayList<>();

        for (String name: categoryNames) {
            Category category = new Category();
            category.setName(name);
            category.setCreatedAt(new Date());
            category.setUpdatedAt(new Date());

            categoryArrayList.add(categoryRepository.save(category));
        }

        return categoryArrayList;
    }

    public Category createACategory(String categoryName) {
        Category category = categoryRepository.getByName(categoryName);

        if(category == null) {
            category = new Category();
            category.setName(categoryName);
            category.setCreatedAt(new Date());
            category.setUpdatedAt(new Date());

            category = categoryRepository.save(category);
        }

        return category;
    }
}
