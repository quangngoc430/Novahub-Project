package vn.novahub.helpdesk.repository;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import vn.novahub.helpdesk.model.DayOff;

@Repository
public interface DayOffRepository extends PagingAndSortingRepository<DayOff, Long>{

    Page<DayOff> findByAccountId(long accountId, Pageable pageable);

    Page<DayOff> findByAccountIdAndStatus(long accountId, String status, Pageable pageable);

    @Query("SELECT dayOff FROM DayOff dayOff WHERE " +
            "dayOff.accountId = :accountId AND " +
            "dayOff.status <> 'CANCELLED'")
    Page<DayOff> findNonCancelledByAccountId(@Param("accountId") long accountId, Pageable pageable);


}
