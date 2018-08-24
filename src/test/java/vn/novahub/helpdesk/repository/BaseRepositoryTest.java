package vn.novahub.helpdesk.repository;

import com.fasterxml.jackson.core.type.TypeReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import vn.novahub.helpdesk.BaseTest;
import vn.novahub.helpdesk.model.*;

import java.io.IOException;
import java.util.List;

@DataJpaTest
@SpringBootTest
public abstract class BaseRepositoryTest extends BaseTest {
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
