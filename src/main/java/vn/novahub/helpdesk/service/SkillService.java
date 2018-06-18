package vn.novahub.helpdesk.service;

import vn.novahub.helpdesk.model.Skill;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;

public interface SkillService {

    Skill createSkillByAccount(Skill skill, long accountId);

    Skill createSkill(Skill skill);

    Skill updateSkill(Skill skill, long accountId, long skillId);

    ArrayList<Skill> getAllSkillsOfACategoryByCategoryId(long categoryId, HttpServletRequest request);

    Skill getSkillBySkillId(long skillId);

    Skill getASkillByCategoryIdAndSkillId(long categoryId, long skillId, HttpServletRequest request);
}
