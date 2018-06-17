package vn.novahub.helpdesk.service;

import vn.novahub.helpdesk.model.Skill;

public interface SkillService {

    Skill createSkillByAccount(Skill skill, long accountId);

    Skill createSkill(Skill skill);

}
