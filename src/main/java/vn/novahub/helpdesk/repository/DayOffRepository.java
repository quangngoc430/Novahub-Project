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

    @Query("SELECT dayOff FROM DayOff dayOff WHERE " +
            "dayOff.accountId = :accountid AND " +
            "dayOff.type LIKE :typekeyword AND dayOff.status LIKE :statuskeyword")
    Page<DayOff> getAllByAccountIdAndTypeLikeAndStatusLike(
            @Param("accountid") long accountId,
            @Param("typekeyword") String typeKeyword,
            @Param("statuskeyword") String statusKeyword,
            Pageable pageable);

    @Query("FROM DayOff dayOff WHERE " +
            "dayOff.type LIKE :typekeyword AND dayOff.status LIKE :statuskeyword")
    Page<DayOff> getAllByTypeLikeAndStatusLike(
            @Param("typekeyword") String typeKeyword,
            @Param("statuskeyword") String statusKeyword,
            Pageable pageable);
}
