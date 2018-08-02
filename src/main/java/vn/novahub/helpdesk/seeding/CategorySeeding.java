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

    private static String[] categoryNames = new String[] {
            "Industry Knowledge",
            "Tools & Technologies",
            "Interpersonal Skills",
            "Front-end Development",
            "Back-end Development",
            "Web Design",
            "Mobile Development",
            "Programing Language",
            };

    public ArrayList<Category> generateData() {

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
}
