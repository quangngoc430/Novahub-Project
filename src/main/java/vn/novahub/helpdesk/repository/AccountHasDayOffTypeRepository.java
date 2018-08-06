package vn.novahub.helpdesk.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import vn.novahub.helpdesk.model.AccountHasDayOffType;

@Repository
public interface AccountHasDayOffTypeRepository extends PagingAndSortingRepository <AccountHasDayOffType, Long>{

    Page<AccountHasDayOffType> findAllByAccountId(long accountId, Pageable pageable);

    AccountHasDayOffType findByAccountIdAndCommonTypeIdAndYear(long accountId, int commonTypeId, int year);

}
