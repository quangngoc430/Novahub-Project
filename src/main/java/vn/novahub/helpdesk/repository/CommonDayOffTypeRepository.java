package vn.novahub.helpdesk.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vn.novahub.helpdesk.model.CommonDayOffType;

@Repository
public interface CommonDayOffTypeRepository
        extends JpaRepository<CommonDayOffType, Integer> {

}
