package vn.novahub.helpdesk.repository;

import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import vn.novahub.helpdesk.BaseTest;

@DataJpaTest
@SpringBootTest
public abstract class BaseRepositoryTest extends BaseTest {

}
