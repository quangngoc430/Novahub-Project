package vn.novahub.helpdesk.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import vn.novahub.helpdesk.exception.CommonTypeIsNotExistException;
import vn.novahub.helpdesk.model.CommonDayOffType;
import vn.novahub.helpdesk.repository.CommonDayOffTypeRepository;
import vn.novahub.helpdesk.service.CommonDayOffTypeService;

import java.util.List;
import java.util.Optional;

@Service
public class CommonDayOffTypeServiceImpl implements CommonDayOffTypeService{

    @Autowired
    private CommonDayOffTypeRepository commonDayOffTypeRepository;

    @Override
    public List<CommonDayOffType> getAllCommonType() {
        return commonDayOffTypeRepository.findAll();
    }

    @Override
    public CommonDayOffType create(CommonDayOffType commonDayOffType) {
        return commonDayOffTypeRepository.save(commonDayOffType);
    }

    @Override
    public CommonDayOffType update(CommonDayOffType commonDayOffType)
                                   throws CommonTypeIsNotExistException {

        Optional<CommonDayOffType> currentCommonDayOffType
                = commonDayOffTypeRepository.findById(commonDayOffType.getId());

        if (!currentCommonDayOffType.isPresent()) {
            throw new CommonTypeIsNotExistException();
        }

        return commonDayOffTypeRepository.save(currentCommonDayOffType.get());
    }

    @Override
    public CommonDayOffType delete(int commonTypeId)
                                   throws CommonTypeIsNotExistException {

        Optional<CommonDayOffType> currentCommonDayOffType
                = commonDayOffTypeRepository.findById(commonTypeId);

        if (!currentCommonDayOffType.isPresent()) {
            throw new CommonTypeIsNotExistException();
        }

        commonDayOffTypeRepository.delete(currentCommonDayOffType.get());

        return currentCommonDayOffType.get();
    }
}
