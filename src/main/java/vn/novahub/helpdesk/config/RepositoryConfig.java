package vn.novahub.helpdesk.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.rest.core.config.RepositoryRestConfiguration;
import org.springframework.data.rest.webmvc.config.RepositoryRestConfigurerAdapter;
import vn.novahub.helpdesk.model.*;

@Configuration
public class RepositoryConfig extends RepositoryRestConfigurerAdapter {

    @Override
    public void configureRepositoryRestConfiguration(RepositoryRestConfiguration configuration){
        configuration.exposeIdsFor(Role.class);
        configuration.exposeIdsFor(Issue.class);
        configuration.exposeIdsFor(Skill.class);
        configuration.exposeIdsFor(Category.class);
        configuration.exposeIdsFor(Account.class);
        configuration.exposeIdsFor(DayOff.class);
        configuration.exposeIdsFor(AccountHasDayOffType.class);
    }
}
