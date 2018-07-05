package vn.novahub.helpdesk.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import vn.novahub.helpdesk.model.DayOffType;

@Repository
public interface DayOffTypeRepository extends PagingAndSortingRepository <DayOffType, Long>{

    DayOffType findByIdAndAccountId(long id, long accountId);

    DayOffType findByTypeAndAccountId(String type, long accountId);

    DayOffType getById(long id);

    Page<DayOffType> findByAccountId(long accountId, Pageable pageable);
}
