package vn.novahub.helpdesk.seeding;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.aspectj.util.FileUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@Component
public class Seeder {

    @Autowired
    private ResourceLoader resourceLoader;

    public <T> List<T> generate(String fileName,
                                             Class<T> classOnWhichArrayIsDefined)
            throws IOException, ClassNotFoundException {

        Resource res = new ClassPathResource(fileName);
        ObjectMapper mapper = new ObjectMapper();

        Class<T[]> arrayClass = (Class<T[]>) Class.forName("[L" + classOnWhichArrayIsDefined.getName() + ";");

        T[] objects = mapper.readValue(res.getFile(), arrayClass);
        return Arrays.asList(objects);
    }


}
