package vn.novahub.helpdesk.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import vn.novahub.helpdesk.exception.DayOffTypeIsExistException;
import vn.novahub.helpdesk.exception.DayOffTypeIsNotValidException;
import vn.novahub.helpdesk.exception.DayOffTypeNotFoundException;
import vn.novahub.helpdesk.model.DayOffType;
import vn.novahub.helpdesk.repository.DayOffTypeRepository;
import vn.novahub.helpdesk.service.DayOffTypeFactory;
import vn.novahub.helpdesk.service.DayOffTypeService;

@Service
public class DayOffTypeServiceImpl implements DayOffTypeService {
    @Override
    public DayOffType add(DayOffType dayOffType) throws DayOffTypeIsExistException, DayOffTypeIsNotValidException {
        return null;
    }

    @Override
    public void update(DayOffType dayOffType) throws DayOffTypeNotFoundException {

    }

    @Override
    public void delete(DayOffType dayOffType) {

    }

    @Override
    public DayOffType getById(long typeId) throws DayOffTypeNotFoundException {
        return null;
    }

    @Override
    public Page<DayOffType> findByAccountId(long accountId, Pageable pageable) {
        return null;
    }
    //    @Autowired
//    private DayOffTypeRepository dayOffTypeRepository;
//
//    @Autowired
//    private DayOffTypeFactory dayOffTypeFactory;
//
//    @Override
//    public DayOffType add(DayOffType dayOffType) throws DayOffTypeIsExistException, DayOffTypeIsNotValidException {
//
//        DayOffType existDayOffType = dayOffTypeRepository
//                                     .findByAccountIdAndTypeAndYear(
//                                             dayOffType.getAccountId(),
//                                             dayOffType.getType(),
//                                             dayOffType.getYear());
//
//        if (existDayOffType == null) {
//            DayOffType newDayOffType = dayOffTypeFactory.create(dayOffType.getType());
//            return dayOffTypeRepository.save(newDayOffType);
//        } else {
//            throw new DayOffTypeIsExistException(dayOffType.getType());
//        }
//
//    }
//
//    @Override
//    public void update(DayOffType dayOffType) throws DayOffTypeNotFoundException{
//        DayOffType existDayOffType = dayOffTypeRepository
//                                     .findByAccountIdAndTypeAndYear(
//                                             dayOffType.getAccountId(),
//                                             dayOffType.getType(),
//                                             dayOffType.getYear());
//
//        if (existDayOffType != null) {
//            existDayOffType.setQuota(dayOffType.getQuota());
//            dayOffTypeRepository.save(existDayOffType);
//        } else {
//            throw new DayOffTypeNotFoundException(dayOffType.getType());
//        }
//    }
//
//    @Override
//    public void delete(DayOffType dayOffType) {
//        dayOffTypeRepository.delete(dayOffType);
//    }
//
//    @Override
//    public DayOffType getById(long typeId) throws DayOffTypeNotFoundException {
//        DayOffType dayOffType = dayOffTypeRepository.getById(typeId);
//
//        if (dayOffType != null) {
//            return dayOffType;
//        } else {
//            throw new DayOffTypeNotFoundException(typeId);
//        }
//    }
//
//    @Override
//    public Page<DayOffType> findByAccountId(long accountId, Pageable pageable) {
//        return dayOffTypeRepository.findByAccountId(accountId, pageable);
//    }
}
