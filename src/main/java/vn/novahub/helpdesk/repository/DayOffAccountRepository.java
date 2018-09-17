package vn.novahub.helpdesk.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import vn.novahub.helpdesk.model.DayOffAccount;

import java.util.List;

@Repository
public interface DayOffAccountRepository extends PagingAndSortingRepository <DayOffAccount, Long>{

    Page<DayOffAccount> findAllByAccountId(long accountId, Pageable pageable);

    List<DayOffAccount> findAllByAccountIdAndYear(long accountId, int year);

    List<DayOffAccount> findByDayOffTypeId(int dayOffTypeId);

    DayOffAccount findByAccountIdAndDayOffTypeIdAndYear(long accountId, int dayOffTypeId, int year);

}
