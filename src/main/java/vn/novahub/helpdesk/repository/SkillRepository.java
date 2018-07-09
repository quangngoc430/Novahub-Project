package vn.novahub.helpdesk.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import vn.novahub.helpdesk.model.Skill;

import javax.transaction.Transactional;

@Repository
@Transactional
public interface SkillRepository extends PagingAndSortingRepository<Skill, Long> {

    Page<Skill> getAllByCategoryId(long categoryId, Pageable pageable);

    @Query("FROM Skill skill " +
            "JOIN AccountHasSkill accountHasSkill ON skill.id = accountHasSkill.skillId " +
            "WHERE accountHasSkill.accountId = :accountId AND skill.name LIKE :nameSkill")
    Page<Skill> getAllSkillsByAccountIdAndNameLike(@Param("accountId") long accountId,
                                                   @Param("name") String nameSkill,
                                                   Pageable pageable);

    @Query("FROM Skill skill JOIN AccountHasSkill accountHasSkill ON skill.id = accountHasSkill.skillId WHERE accountHasSkill.accountId = :accountId AND skill.categoryId = :categoryId")
    Page<Skill> getAllByAccountId(@Param("accountId") long accountId,
                                  @Param("categoryId") long categoryId,
                                  Pageable pageable);

    @Query("FROM Skill skill WHERE skill.name LIKE :name")
    Page<Skill> getAllSkillsByNameLike(@Param("name") String name,
                                       Pageable pageable);

    Page<Skill> getAllByCategoryIdAndNameLike(long categoryId, String name, Pageable pageable);

    Skill getByName(String skillName);

    Skill getByIdAndCategoryId(long skillId, long categoryId);

    void deleteByIdAndCategoryId(long skillId, long categoryId);

    boolean existsByIdAndCategoryId(long skillId, long categoryId);

    Skill getById(long skillId);

    boolean existsByName(String email);

}
