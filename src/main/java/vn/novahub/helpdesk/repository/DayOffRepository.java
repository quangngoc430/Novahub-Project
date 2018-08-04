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

    @Query("SELECT dayOff " +
            "FROM DayOff dayOff " +
            "JOIN Account account " +
            "ON dayOff.accountId = account.id " +
            "JOIN DayOffType dayOffType " +
            "ON dayOff.typeId = dayOffType.id " +
            "JOIN CommonDayOffType commonDayOffType " +
            "ON dayOffType.commonTypeId = commonDayOffType.id " +
            "WHERE commonDayOffType.type LIKE CONCAT('%', :keyword ,'%') " +
            "OR account.email LIKE CONCAT('%', :keyword, '%')")
    Page<DayOff> findByKeyword(@Param("keyword") String keyword,
                                                Pageable pageable);

    @Query("SELECT dayOff " +
            "FROM DayOff dayOff " +
            "JOIN Account account " +
            "ON dayOff.accountId = account.id " +
            "JOIN DayOffType dayOffType " +
            "ON dayOff.typeId = dayOffType.id " +
            "JOIN CommonDayOffType commonDayOffType " +
            "ON dayOffType.commonTypeId = commonDayOffType.id " +
            "WHERE (commonDayOffType.type LIKE CONCAT('%', :keyword ,'%') " +
            "OR account.email LIKE CONCAT('%', :keyword, '%')) " +
            "AND dayOff.status <> 'CANCELLED'")
    Page<DayOff> findNonCancelledByKeyword( @Param("keyword") String keyword,
                                Pageable pageable);
}
