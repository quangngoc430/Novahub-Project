package vn.novahub.helpdesk;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.rest.core.config.RepositoryRestConfiguration;
import org.springframework.data.rest.webmvc.config.RepositoryRestConfigurerAdapter;
import vn.novahub.helpdesk.model.Category;
import vn.novahub.helpdesk.model.Issue;
import vn.novahub.helpdesk.model.Role;
import vn.novahub.helpdesk.model.Skill;

@Configuration
public class RepositoryConfig extends RepositoryRestConfigurerAdapter {

    @Override
    public void configureRepositoryRestConfiguration(RepositoryRestConfiguration configuration){
        configuration.exposeIdsFor(Role.class);
        configuration.exposeIdsFor(Issue.class);
        configuration.exposeIdsFor(Skill.class);
        configuration.exposeIdsFor(Category.class);
    }
}
