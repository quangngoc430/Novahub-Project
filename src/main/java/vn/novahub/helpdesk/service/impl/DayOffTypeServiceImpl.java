package vn.novahub.helpdesk.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import vn.novahub.helpdesk.exception.DayOffTypeIsNotExistException;
import vn.novahub.helpdesk.model.DayOffType;
import vn.novahub.helpdesk.repository.DayOffTypeRepository;
import vn.novahub.helpdesk.service.DayOffTypeService;

import java.util.List;
import java.util.Optional;

@Service
public class DayOffTypeServiceImpl implements DayOffTypeService {

    @Autowired
    private DayOffTypeRepository dayOffTypeRepository;

    @Override
    public List<DayOffType> getAllDayOffType() {
        return dayOffTypeRepository.findAll();
    }

    @Override
    public DayOffType getById(int id) throws DayOffTypeIsNotExistException {
        Optional<DayOffType> dayOffTypeOptional = dayOffTypeRepository.findById(id);

        if (!dayOffTypeOptional.isPresent()) {
            throw new DayOffTypeIsNotExistException();
        }
        return dayOffTypeOptional.get();
    }

    @Override
    public DayOffType create(DayOffType dayOffType) {
        return dayOffTypeRepository.save(dayOffType);
    }

    @Override
    public DayOffType update(DayOffType dayOffType)
                                   throws DayOffTypeIsNotExistException {

        Optional<DayOffType> currentDayOffType
                = dayOffTypeRepository.findById(dayOffType.getId());

        if (!currentDayOffType.isPresent()) {
            throw new DayOffTypeIsNotExistException();
        }

        if (dayOffType.getType() != null) {
            currentDayOffType.get().setType(dayOffType.getType());
        }

        if (dayOffType.getDefaultQuota() > 0) {
            currentDayOffType.get().setDefaultQuota(dayOffType.getDefaultQuota());
        }

        return dayOffTypeRepository.save(currentDayOffType.get());
    }

    @Override
    public DayOffType delete(int id) throws DayOffTypeIsNotExistException {

        Optional<DayOffType> currentDayOffType
                = dayOffTypeRepository.findById(id);

        if (!currentDayOffType.isPresent()) {
            throw new DayOffTypeIsNotExistException();
        }

        dayOffTypeRepository.delete(currentDayOffType.get());

        return currentDayOffType.get();
    }
}
