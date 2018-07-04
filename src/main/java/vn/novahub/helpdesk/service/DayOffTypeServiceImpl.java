package vn.novahub.helpdesk.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
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
    public void modifyQuota(DayOffType dayOffType, int quota) {
        dayOffType.setQuota(quota);
        dayOffTypeRepository.save(dayOffType);
    }

    @Override
    public void deleteDayOffType(DayOffType dayOffType) {
        dayOffTypeRepository.delete(dayOffType);
    }

    @Override
    public DayOffType findByAccountIdAndType(long accountId, String type) {
        return dayOffTypeRepository.findByAccountIdAndType(accountId, type);
    }
}
