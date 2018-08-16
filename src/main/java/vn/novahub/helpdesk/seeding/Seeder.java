package vn.novahub.helpdesk.seeding;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;

@Component
public class Seeder {

//    public <T> List<T> generate(String fileName,
//                                             Class<T> classOnWhichArrayIsDefined)
//            throws IOException, ClassNotFoundException {
//
//        Resource res = new ClassPathResource(fileName);
//        ObjectMapper mapper = new ObjectMapper();
//
//        Class<T[]> arrayClass = (Class<T[]>) Class.forName("[L" + classOnWhichArrayIsDefined.getName() + ";");
//
//        T[] objects = mapper.readValue(res.getFile(), arrayClass);
//        return Arrays.asList(objects);
//    }

    List generate(String fileName, TypeReference typeReference)
            throws IOException {

        ObjectMapper mapper = new ObjectMapper();
        InputStream inputStream = TypeReference.class.getResourceAsStream("/" + fileName);

        return mapper.readValue(inputStream, typeReference);
    }

}
