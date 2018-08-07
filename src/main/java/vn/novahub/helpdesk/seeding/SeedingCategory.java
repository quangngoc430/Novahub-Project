package vn.novahub.helpdesk.seeding;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;
import vn.novahub.helpdesk.model.Category;
import vn.novahub.helpdesk.repository.CategoryRepository;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

@Component
public class SeedingCategory {

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private ResourceLoader resourceLoader;

    public ArrayList<Category> generateData(String fileName) throws IOException {

        ArrayList<Category> categoryArrayList = new ArrayList<>();

        ObjectMapper objectMapper = new ObjectMapper();
        Resource res = resourceLoader.getResource("classpath:" + fileName);
        JsonNode jsonNodeRoot = objectMapper.readValue(res.getFile(), JsonNode.class);
        JsonNode jsonNodeCategory = jsonNodeRoot.get("category").get("name");

        for (int i = 0; i < jsonNodeCategory.size(); i++) {
            String categoryName = jsonNodeCategory.get(i).textValue();

            Category category = categoryRepository.getByName(categoryName);

            if(category == null) {
                category = new Category();
                category.setName(categoryName);
                category.setCreatedAt(new Date());
                category.setUpdatedAt(new Date());
                category = categoryRepository.save(category);
            }

            categoryArrayList.add(category);
        }

        return categoryArrayList;
    }
}