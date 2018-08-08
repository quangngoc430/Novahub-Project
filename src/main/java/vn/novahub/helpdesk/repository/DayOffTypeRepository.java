package vn.novahub.helpdesk.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vn.novahub.helpdesk.model.DayOffType;

@Repository
public interface DayOffTypeRepository
        extends JpaRepository<DayOffType, Integer> {

    DayOffType findByType(String type);

}
