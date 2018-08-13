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

    @Query("SELECT dayOff " +
            "FROM DayOff dayOff " +
            "JOIN DayOffAccount dayOffAccount " +
            "ON dayOff.dayOffAccountId = dayOffAccount.id " +
            "WHERE dayOffAccount.accountId = :accountId")
    Page<DayOff> findAllByAccountId(@Param("accountId") long accountId,
                                    Pageable pageable);

    @Query("SELECT dayOff " +
            "FROM DayOff dayOff " +
            "JOIN DayOffAccount dayOffAccount " +
            "ON dayOff.dayOffAccountId = dayOffAccount.id " +
            "WHERE dayOffAccount.accountId = :accountId " +
            "AND dayOff.status = :status")
    Page<DayOff> findByAccountIdAndStatus(@Param("accountId") long accountId,
                                           @Param("status") String status,
                                            Pageable pageable);

    @Query("SELECT dayOff " +
            "FROM DayOff dayOff " +
            "JOIN DayOffAccount dayOffAccount " +
            "ON dayOff.dayOffAccountId = dayOffAccount.id " +
            "WHERE dayOffAccount.accountId = :accountId " +
            "AND dayOff.status <> 'CANCELLED'")
    Page<DayOff> findNonCancelledByAccountId(@Param("accountId") long accountId, Pageable pageable);

    @Query("SELECT dayOff " +
            "FROM DayOff dayOff " +
            "JOIN DayOffAccount dayOffAccount " +
            "ON dayOff.dayOffAccountId = dayOffAccount.id " +
            "JOIN DayOffType dayOffType " +
            "ON dayOffAccount.dayOffTypeId = dayOffType.id " +
            "JOIN Account account " +
            "ON dayOffAccount.accountId = account.id " +
            "WHERE dayOffType.type LIKE CONCAT('%', :keyword ,'%') " +
            "OR account.email LIKE CONCAT('%', :keyword, '%')")
    Page<DayOff> findByKeyword(@Param("keyword") String keyword,
                                                Pageable pageable);


    @Query("SELECT dayOff " +
            "FROM DayOff dayOff " +
            "JOIN DayOffAccount dayOffAccount " +
            "ON dayOff.dayOffAccountId = dayOffAccount.id " +
            "JOIN DayOffType dayOffType " +
            "ON dayOffAccount.dayOffTypeId = dayOffType.id " +
            "JOIN Account account " +
            "ON dayOffAccount.accountId = account.id " +
            "WHERE (dayOffType.type LIKE CONCAT('%', :keyword ,'%') " +
            "OR account.email LIKE CONCAT('%', :keyword, '%')) " +
            "AND dayOff.status <> 'CANCELLED'")
    Page<DayOff> findNonCancelledByKeyword( @Param("keyword") String keyword,
                                Pageable pageable);
}
