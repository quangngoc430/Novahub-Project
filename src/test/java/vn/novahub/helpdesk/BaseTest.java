package vn.novahub.helpdesk;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.runner.RunWith;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import vn.novahub.helpdesk.seeding.ApplicationSeeder;

import java.io.File;
import java.io.IOException;
import java.util.List;

@ActiveProfiles("test")
@RunWith(SpringRunner.class)
public abstract class BaseTest {

    @MockBean
    private ApplicationSeeder seeder;

    public <T> List<T> convertJsonFileToObjectList(String fileName, TypeReference typeReference) throws IOException {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        ObjectMapper mapper = new ObjectMapper();
        File file = new File(classLoader.getResource(fileName).getFile());
        return mapper.readValue(file, typeReference);
    }
}
