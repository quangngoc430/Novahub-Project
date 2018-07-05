package vn.novahub.helpdesk.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import vn.novahub.helpdesk.exception.DayOffTypeNotFoundException;
import vn.novahub.helpdesk.model.DayOffType;
import vn.novahub.helpdesk.repository.DayOffTypeRepository;

@Service
public class DayOffTypeServiceImpl implements DayOffTypeService {

    @Autowired
    private DayOffTypeRepository dayOffTypeRepository;

    @Override
    public void addNewDayOffType(DayOffType dayOffType) {
        dayOffTypeRepository.save(dayOffType);
    }

    @Override
    public void modifyQuota(DayOffType dayOffType) throws DayOffTypeNotFoundException{
        String type = dayOffType.getType();
        dayOffType = dayOffTypeRepository.findByTypeAndAccountId(dayOffType.getType(), dayOffType.getAccountId());
        if (dayOffType != null) {
            dayOffTypeRepository.save(dayOffType);
        } else {
            throw new DayOffTypeNotFoundException(type);
        }
    }

    @Override
    public void deleteDayOffType(DayOffType dayOffType) {
        dayOffTypeRepository.delete(dayOffType);
    }

    @Override
    public DayOffType findByIdAndAccountId(long typeId, long accountId) {
        return dayOffTypeRepository.findByIdAndAccountId(typeId, accountId);
    }

    @Override
    public Page<DayOffType> findByAccountId(long accountId, Pageable pageable) {
        return dayOffTypeRepository.findByAccountId(accountId, pageable);
    }
}
