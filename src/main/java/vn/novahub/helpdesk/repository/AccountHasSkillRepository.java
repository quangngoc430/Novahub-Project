package vn.novahub.helpdesk.repository;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import vn.novahub.helpdesk.model.AccountHasSkill;

@Repository
public interface AccountHasSkillRepository extends PagingAndSortingRepository<AccountHasSkill, Long> {
}
