package vn.novahub.helpdesk.service;

import vn.novahub.helpdesk.model.Skill;

public interface SkillService {

    Skill createSkillByAccount(Skill skill, long accountId);

    Skill createSkill(Skill skill);

    Skill updateSkill(Skill skill, long accountId, long skillId);

    Skill getSkillBySkillId(long skillId);
}
