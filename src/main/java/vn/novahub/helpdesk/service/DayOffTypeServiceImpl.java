package vn.novahub.helpdesk.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import vn.novahub.helpdesk.exception.DayOffTypeIsExistException;
import vn.novahub.helpdesk.exception.DayOffTypeNotFoundException;
import vn.novahub.helpdesk.model.DayOffType;
import vn.novahub.helpdesk.repository.DayOffTypeRepository;

@Service
public class DayOffTypeServiceImpl implements DayOffTypeService {

    @Autowired
    private DayOffTypeRepository dayOffTypeRepository;

    @Override
    public void add(DayOffType dayOffType) throws DayOffTypeIsExistException {
        DayOffType existDayOffType = dayOffTypeRepository
                                     .findByTypeAndAccountId(dayOffType.getType(), dayOffType.getAccountId());

        if (existDayOffType == null) {
            dayOffType.setRemainingTime(dayOffType.getQuota());
            dayOffTypeRepository.save(dayOffType);
        } else {
            throw new DayOffTypeIsExistException(dayOffType.getType());
        }

    }

    @Override
    public void update(DayOffType dayOffType) throws DayOffTypeNotFoundException{
        DayOffType existDayOffType = dayOffTypeRepository
                                     .findByTypeAndAccountId(dayOffType.getType(), dayOffType.getAccountId());

        if (existDayOffType != null) {
            existDayOffType.setQuota(dayOffType.getQuota());
            dayOffTypeRepository.save(existDayOffType);
        } else {
            throw new DayOffTypeNotFoundException(dayOffType.getType());
        }
    }

    @Override
    public void delete(DayOffType dayOffType) {
        dayOffTypeRepository.delete(dayOffType);
    }

    @Override
    public DayOffType findById(long typeId) throws DayOffTypeNotFoundException {
        DayOffType dayOffType = dayOffTypeRepository.getById(typeId);

        if (dayOffType != null) {
            return dayOffType;
        } else {
            throw new DayOffTypeNotFoundException(typeId);
        }
    }

    @Override
    public Page<DayOffType> findByAccountId(long accountId, Pageable pageable) {
        return dayOffTypeRepository.findByAccountId(accountId, pageable);
    }
}
