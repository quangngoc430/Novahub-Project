package vn.novahub.helpdesk.repository;


import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import vn.novahub.helpdesk.model.DayOff;

@Repository
public interface DayOffRepository extends PagingAndSortingRepository<DayOff, Long>{

}
