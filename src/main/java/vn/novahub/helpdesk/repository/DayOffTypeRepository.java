package vn.novahub.helpdesk.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import vn.novahub.helpdesk.model.DayOffType;

@Repository
public interface DayOffTypeRepository extends PagingAndSortingRepository <DayOffType, Long>{

    DayOffType findByAccountIdAndType(long accountId, String type);

    Page<DayOffType> findByAccountId(long accountId);
}
