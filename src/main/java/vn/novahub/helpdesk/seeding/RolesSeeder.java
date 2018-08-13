package vn.novahub.helpdesk.seeding;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;
import vn.novahub.helpdesk.model.Role;
import vn.novahub.helpdesk.repository.RoleRepository;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;

@Component
public class RolesSeeder {

    @Autowired
    private ResourceLoader resourceLoader;

    @Autowired
    private RoleRepository roleRepository;

    public ArrayList<Role> generateData(String fileName) throws IOException, ParseException {
        ArrayList<Role> roleArrayList = new ArrayList<>();

        ObjectMapper objectMapper = new ObjectMapper();
        Resource res = resourceLoader.getResource("classpath:" + fileName);
        JsonNode jsonNodeRoot = objectMapper.readValue(res.getFile(), JsonNode.class);
        JsonNode jsonNodeRole = jsonNodeRoot.get("role");

        for(int i = 0; i < jsonNodeRole.size(); i++) {
            Role role = new Role();
            role.setName(jsonNodeRole.get(i).textValue());
            roleArrayList.add(roleRepository.save(role));
        }

        return roleArrayList;
    }

}
