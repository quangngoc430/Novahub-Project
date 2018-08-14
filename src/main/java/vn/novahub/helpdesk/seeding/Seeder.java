package vn.novahub.helpdesk.seeding;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class Seeder<T> {

    @Autowired
    private ResourceLoader resourceLoader;

    public List<T> parseJsonArray(String fileName,
                                             Class<T> classOnWhichArrayIsDefined)
            throws IOException, ClassNotFoundException {

        Resource res = resourceLoader.getResource("classpath:" + fileName);
        ObjectMapper mapper = new ObjectMapper();
        Class<T[]> arrayClass = (Class<T[]>) Class.forName("[L" + classOnWhichArrayIsDefined.getName() + ";");
        T[] objects = mapper.readValue(fileName, arrayClass);
        return Arrays.asList(objects);
    }
}
