package vn.novahub.helpdesk;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import vn.novahub.helpdesk.model.*;
import vn.novahub.helpdesk.repository.*;
import vn.novahub.helpdesk.seeding.ApplicationSeeder;
import vn.novahub.helpdesk.seeding.Seeder;

import java.io.File;
import java.io.IOException;
import java.util.List;

@ActiveProfiles("test")
@RunWith(SpringRunner.class)
public abstract class BaseTest {

    @MockBean
    private ApplicationSeeder seeder;

    @Autowired
    protected DayOffRepository dayOffRepository;

    @Autowired
    protected DayOffTypeRepository dayOffTypeRepository;

    @Autowired
    protected DayOffAccountRepository dayOffAccountRepository;

    @Autowired
    protected RoleRepository roleRepository;

    @Autowired
    protected AccountRepository accountRepository;

    public <T> List<T> convertJsonFileToObjectList(String fileName, TypeReference typeReference) throws IOException {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        ObjectMapper mapper = new ObjectMapper();
        File file = new File(classLoader.getResource(fileName).getFile());
        return mapper.readValue(file, typeReference);
    }

    public void initData() throws IOException {

        roleRepository.saveAll(convertJsonFileToObjectList(
                "seeding/roles.json",
                new TypeReference<List<Role>>(){}));

        accountRepository.saveAll(convertJsonFileToObjectList(
                "seeding/accounts.json",
                new TypeReference<List<Account>>(){}));

        dayOffTypeRepository.saveAll(convertJsonFileToObjectList(
                "seeding/day_off_type.json",
                new TypeReference<List<DayOffType>>(){}));

        dayOffAccountRepository.saveAll(convertJsonFileToObjectList(
                "seeding/day_off_account.json",
                new TypeReference<List<DayOffAccount>>(){}));

        dayOffRepository.saveAll(convertJsonFileToObjectList(
                "seeding/day_off.json",
                new TypeReference<List<DayOff>>(){}));

    }

}
