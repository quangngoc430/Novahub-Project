package vn.novahub.helpdesk.seeding;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;
import vn.novahub.helpdesk.model.Category;
import vn.novahub.helpdesk.model.Skill;
import vn.novahub.helpdesk.repository.CategoryRepository;
import vn.novahub.helpdesk.repository.SkillRepository;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

@Component
public class SkillsSeeder {

    @Autowired
    private SkillRepository skillRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private ResourceLoader resourceLoader;

    public ArrayList<Skill> generateData(String fileName) throws IOException {
        ArrayList<Skill> skillArrayList = new ArrayList<>();

        ObjectMapper objectMapper = new ObjectMapper();
        Resource res = resourceLoader.getResource("classpath:" + fileName);
        JsonNode jsonNodeRoot = objectMapper.readValue(res.getFile(), JsonNode.class);
        JsonNode jsonNodeSkill = jsonNodeRoot.get("skill");

        for(int i = 0; i < jsonNodeSkill.size(); i++) {
            JsonNode currentJsonNode = jsonNodeSkill.get(i);

            String categoryName = currentJsonNode.get("category").textValue();

            Category category = categoryRepository.getByName(categoryName);

            for (int skillIndex = 0; skillIndex < currentJsonNode.get("name").size(); skillIndex++) {
                String skillName = currentJsonNode.get("name").get(skillIndex).textValue();

                Skill skill = skillRepository.getByNameAndCategoryId(skillName, category.getId());

                if(skill == null) {
                    skill = new Skill();
                    skill.setName(skillName);
                    skill.setCategoryId(category.getId());
                    skill.setCreatedAt(new Date());
                    skill.setUpdatedAt(new Date());
                    skill = skillRepository.save(skill);
                }

                skillArrayList.add(skill);
            }
        }

        return skillArrayList;
    }
}