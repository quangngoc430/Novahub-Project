package vn.novahub.helpdesk.seeding;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@Component
public class Seeder {


    List generate(String fileName, TypeReference typeReference)
            throws IOException {

        ObjectMapper mapper = new ObjectMapper();
        InputStream inputStream = TypeReference.class.getResourceAsStream("/" + fileName);

        return mapper.readValue(inputStream, typeReference);
    }

}
