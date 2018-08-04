package vn.novahub.helpdesk.seeding;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import vn.novahub.helpdesk.repository.SkillRepository;

@Component
public class SkillSeeding {

    @Autowired
    private SkillRepository skillRepository;
}
