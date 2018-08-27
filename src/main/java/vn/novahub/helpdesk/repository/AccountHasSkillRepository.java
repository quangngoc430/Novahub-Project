package vn.novahub.helpdesk.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import vn.novahub.helpdesk.model.AccountHasSkill;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Repository
@Transactional
public interface AccountHasSkillRepository extends PagingAndSortingRepository<AccountHasSkill, Long> {

    AccountHasSkill findByAccountIdAndSkillId(long accountId, long skillId);

    AccountHasSkill getByAccountIdAndSkillId(long accountId, long skillId);

    ArrayList<AccountHasSkill> findBySkillId(long skillId);

    void deleteByAccountIdAndSkillId(long accountId, long skillId);

    long countBySkillId(long skillId);

    boolean existsByAccountIdAndSkillId(long accountId, long skillId);

    @Query("SELECT distinct accountHasSkill " +
           "FROM AccountHasSkill accountHasSkill " +
           "WHERE accountHasSkill.accountId IN :accountIds ")
    List<AccountHasSkill> getAllByAccountIdIn(@Param("accountIds") List<Long> accountIds);
}
