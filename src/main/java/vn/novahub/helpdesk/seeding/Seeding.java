package vn.novahub.helpdesk.seeding;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;
import vn.novahub.helpdesk.model.Category;

import java.io.IOException;
import java.util.ArrayList;

@Component
public class Seeding {

    @Autowired
    private ResourceLoader resourceLoader;

    @Autowired
    private CategorySeeding categorySeeding;

    public void readJsonWithObjectMapper() throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        Resource res = resourceLoader.getResource("classpath:data.json");
        JsonNode jsonRoot = objectMapper.readValue(res.getFile(), JsonNode.class);

        ArrayList<String> categoryNames = new ArrayList<>();

        ArrayList<Category> categoryArrayList = new ArrayList<>();

        JsonNode jsonNodeCategory = jsonRoot.get("category");

        for(int categoryIndex = 0; categoryIndex < jsonNodeCategory.size(); categoryIndex++) {
            //Category category = categorySeeding.createACategory(jsonNodeCategory.get(categoryIndex).get("name").textValue());


        }

//        JsonNode jsonNode = categoryList.
        System.out.println("d");
    }
}
