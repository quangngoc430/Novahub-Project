package vn.novahub.helpdesk.service;

import vn.novahub.helpdesk.model.Skill;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;

public interface SkillService {

    Skill createSkillByAccount(Skill skill, long accountId);

    Skill createASkillOfACategory(Skill skill, long categoryId, HttpServletRequest request);

    Skill updateSkill(Skill skill, long accountId, long skillId);

    Skill updateSkillByCategoryIdAndSkillId(Skill skill, long categoryId, long skillId, HttpServletRequest request);

    ArrayList<Skill> getAllSkillsOfACategoryByCategoryId(long categoryId, HttpServletRequest request);

    Skill getSkillBySkillId(long skillId);

    Skill getASkillByCategoryIdAndSkillId(long categoryId, long skillId, HttpServletRequest request);

    void deteleASkillByCategoryIdAndSkillId(long categoryId, long skillId, HttpServletRequest request);

}
