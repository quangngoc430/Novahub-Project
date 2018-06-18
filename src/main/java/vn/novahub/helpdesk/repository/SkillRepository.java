package vn.novahub.helpdesk.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import vn.novahub.helpdesk.model.Skill;

import javax.transaction.Transactional;
import java.util.ArrayList;

@Repository
public interface SkillRepository extends PagingAndSortingRepository<Skill, Long> {

    @Query("FROM Skill skill")
    ArrayList<Skill> getAllSkills();

    ArrayList<Skill> getAllByCategoryId(long categoryId);

    @Query("FROM Skill skill JOIN AccountHasSkill accountHasSkill ON skill.id = accountHasSkill.skillId WHERE accountHasSkill.accountId = :accountId")
    ArrayList<Skill> getAllSkillsByAccountId(@Param("accountId") long accountId);

    @Query("FROM Skill skill JOIN AccountHasSkill accountHasSkill ON skill.id = accountHasSkill.skillId WHERE accountHasSkill.accountId = :accountId AND skill.categoryId = :categoryId")
    ArrayList<Skill> getAllSkillsByAccountIdAndCategoryId(@Param("accountId") long accountId,
                                                          @Param("categoryId") long categoryId);

    @Query("FROM Skill skill WHERE skill.name LIKE :name")
    ArrayList<Skill> getAllSkillsByNameLike(@Param("name") String name);

    Skill findByName(String skillName);

    Skill findByIdAndCategoryId(long skillId, long categoryId);
}
