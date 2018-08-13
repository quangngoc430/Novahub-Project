package vn.novahub.helpdesk.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import vn.novahub.helpdesk.model.DayOffAccount;

@Repository
public interface DayOffAccountRepository extends PagingAndSortingRepository <DayOffAccount, Long>{

    Page<DayOffAccount> findAllByAccountId(long accountId, Pageable pageable);

    DayOffAccount findByAccountIdAndDayOffTypeIdAndYear(long accountId, int dayOffTypeId, int year);

}
