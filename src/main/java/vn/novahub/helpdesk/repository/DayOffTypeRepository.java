package vn.novahub.helpdesk.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import vn.novahub.helpdesk.model.DayOffType;

@Repository
public interface DayOffTypeRepository extends PagingAndSortingRepository <DayOffType, Long>{

    Page<DayOffType> findAllByAccountId(long accountId, Pageable pageable);

    DayOffType findByAccountIdAndCommonTypeIdAndYear(long accountId, int commonTypeId, int year);

}
