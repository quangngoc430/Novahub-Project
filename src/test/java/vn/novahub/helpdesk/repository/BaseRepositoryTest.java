package vn.novahub.helpdesk.repository;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import vn.novahub.helpdesk.BaseTest;
import vn.novahub.helpdesk.model.*;

import java.io.File;
import java.io.IOException;
import java.util.List;

@DataJpaTest
@SpringBootTest
public abstract class BaseRepositoryTest extends BaseTest {

    @Autowired
    DayOffRepository dayOffRepository;

    @Autowired
    DayOffTypeRepository dayOffTypeRepository;

    @Autowired
    DayOffAccountRepository dayOffAccountRepository;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    AccountRepository accountRepository;

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
