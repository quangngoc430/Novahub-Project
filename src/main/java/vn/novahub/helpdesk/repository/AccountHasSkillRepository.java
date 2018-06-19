package vn.novahub.helpdesk.repository;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import vn.novahub.helpdesk.model.AccountHasSkill;

import java.util.ArrayList;

@Repository
public interface AccountHasSkillRepository extends PagingAndSortingRepository<AccountHasSkill, Long> {

    AccountHasSkill findByAccountIdAndSkillId(long accountId, long skillId);

    ArrayList<AccountHasSkill> findBySkillId(long skillId);
}
