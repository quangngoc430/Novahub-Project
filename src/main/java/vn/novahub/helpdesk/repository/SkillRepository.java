package vn.novahub.helpdesk.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import vn.novahub.helpdesk.model.Skill;

import javax.transaction.Transactional;
import java.util.List;

@Repository
@Transactional
public interface SkillRepository extends PagingAndSortingRepository<Skill, Long> {

    @Query("SELECT new Skill(skill.id, skill.name, accountHasSkill.level, skill.categoryId, skill.createdAt, skill.updatedAt, category) " +
           "FROM Skill skill " +
           "JOIN AccountHasSkill accountHasSkill ON accountHasSkill.skillId = skill.id " +
           "JOIN Category category ON category.id = skill.categoryId " +
           "WHERE accountHasSkill.accountId = :accountId AND skill.name LIKE CONCAT('%', :name, '%')")
    Page<Skill> getAllByNameContainingAndAccountId(@Param("name") String name,
                                                   @Param("accountId") long accountId,
                                                   Pageable pageable);

    @Query("SELECT distinct new Skill(skill.id, skill.name, skill.categoryId, skill.createdAt, skill.updatedAt, category) " +
           "FROM Skill skill " +
           "JOIN Category category ON category.id = skill.categoryId " +
           "WHERE skill.name LIKE CONCAT('%', :name, '%') AND skill.categoryId = :categoryId")
    Page<Skill> getAllByNameContainingAndCategoryId(@Param("name") String name,
                                                    @Param("categoryId") long categoryId,
                                                    Pageable pageable);

    Page<Skill> getAllByNameContaining(String name, Pageable pageable);

    Skill getByNameAndCategoryId(String skillName, long categoryId);

    Skill getByIdAndCategoryId(long skillId, long categoryId);

    boolean existsByIdAndCategoryId(long skillId, long categoryId);

    @Query("SELECT new Skill(skill.id, skill.name, accountHasSkill.level, skill.categoryId, skill.createdAt, skill.updatedAt, category) " +
            "FROM Skill skill " +
            "JOIN AccountHasSkill accountHasSkill ON skill.id = accountHasSkill.skillId " +
            "JOIN Category category ON category.id = skill.categoryId " +
            "WHERE accountHasSkill.accountId = :accountId AND skill.id = :skillId")
    Skill getByAccountIdAndSkillId(@Param("accountId") long accountId,
                                   @Param("skillId") long skillId);

    @Query("SELECT new Skill(skill.id, skill.name, accountHasSkill.level, skill.categoryId, skill.createdAt, skill.updatedAt, category) " +
           "FROM Skill skill " +
           "JOIN AccountHasSkill accountHasSkill ON skill.id = accountHasSkill.skillId " +
           "JOIN Category category ON category.id = skill.categoryId " +
           "WHERE accountHasSkill.accountId = :accountId AND skill.id = :skillId AND skill.categoryId = :categoryId")
    Skill getByAccountIdAndSkillIdAndCategoryId(@Param("accountId") long accountId,
                                                @Param("skillId") long skillId,
                                                @Param("categoryId") long categoryId);

    @Query("SELECT new Skill(skill.id, skill.name, accountHasSkill.level, skill.categoryId, skill.createdAt, skill.updatedAt, category) " +
           "FROM Skill skill " +
           "JOIN AccountHasSkill accountHasSkill ON accountHasSkill.skillId = skill.id " +
           "JOIN Category category ON skill.categoryId = category.id " +
           "WHERE accountHasSkill.accountId = :accountId")
    Page<Skill> getAllByAccountId(@Param("accountId") long accountId,
                                  Pageable pageable);

    boolean deleteByIdAndCategoryId(long skillId, long categoryId);

    Page<Skill> getAllByIdIsIn(List<Long> skillIds, Pageable pageable);
}
