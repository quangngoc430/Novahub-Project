package vn.novahub.helpdesk;

import org.junit.runner.RunWith;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import vn.novahub.helpdesk.seeding.ApplicationSeeder;
import vn.novahub.helpdesk.seeding.Seeder;

@ActiveProfiles("test")
@RunWith(SpringRunner.class)
public abstract class BaseTest {

    @MockBean
    private ApplicationSeeder seeder;

}
