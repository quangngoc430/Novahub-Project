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

    Page<Skill> getAllByNameContaining(String keyword, Pageable pageable);

    @Query("SELECT skill " +
           "FROM Skill skill " +
           "JOIN AccountHasSkill accountHasSkill ON accountHasSkill.skillId = skill.id " +
           "JOIN Level level ON level.skillId = skill.id " +
           "WHERE accountHasSkill.accountId = :accountId AND skill.name LIKE :name")
    Page<Skill> getAllByNameContainingAndAccountId(@Param("name") String name,
                                                   @Param("accountId") long accountId,
                                                   Pageable pageable);

    Page<Skill> getAllByCategoryIdAndNameContaining(long categoryId, String name, Pageable pageable);

    Skill getByName(String skillName);

    Skill getByIdAndCategoryId(long skillId, long categoryId);

    boolean existsByIdAndCategoryId(long skillId, long categoryId);

    Skill getById(long skillId);


    @Query("SELECT skill " +
           "FROM Skill skill " +
           "JOIN AccountHasSkill accountHasSkill " +
           "ON skill.id = accountHasSkill.skillId " +
           "WHERE accountHasSkill.accountId = :accountId AND skill.id = :skillId")
    Skill getByAccountIdAndSkillId(@Param("accountId") long accountId,
                                   @Param("skillId") long skillId);


    @Query("SELECT skill " +
           "FROM Skill skill " +
           "JOIN AccountHasSkill accountHasSkill ON accountHasSkill.skillId = skill.id " +
           "JOIN Level level ON level.skillId = skill.id " +
           "WHERE accountHasSkill.accountId = :accountId")
    Page<Skill> getAllByAccountId(@Param("accountId") long accountId,
                                  Pageable pageable);

    boolean deleteByIdAndCategoryId(long skillId, long categoryId);

}
