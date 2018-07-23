package vn.novahub.helpdesk.impl;

import org.springframework.stereotype.Service;
import vn.novahub.helpdesk.constant.DayOffTypeConstant;
import vn.novahub.helpdesk.exception.DayOffTypeIsNotValidException;
import vn.novahub.helpdesk.model.DayOffType;
import vn.novahub.helpdesk.service.DayOffTypeFactory;

import java.util.ArrayList;
import java.util.List;

@Service
public class DayOffTypeFactoryImpl implements DayOffTypeFactory {

    private List<String> dayOffTypes = new ArrayList<>();
    

    public DayOffTypeFactoryImpl() {
        dayOffTypes.add(DayOffTypeConstant.YEARLY_TYPE);
        dayOffTypes.add(DayOffTypeConstant.SICK_TYPE);
        dayOffTypes.add(DayOffTypeConstant.PREGNANT_TYPE);
    }

    public boolean isValidDayOffType(String type) {
        return dayOffTypes.contains(type);
    }

    public DayOffType create(String type) throws DayOffTypeIsNotValidException{
        DayOffType dayOffType = new DayOffType();
        
        if (!isValidDayOffType(type)) {
            throw new DayOffTypeIsNotValidException(type);
        } else if (type.equalsIgnoreCase(DayOffTypeConstant.YEARLY_TYPE)){
            dayOffType.setQuota(DayOffTypeConstant.QUOTA_OF_YEARLY);
        } else if (type.equalsIgnoreCase(DayOffTypeConstant.SICK_TYPE)){
            dayOffType.setQuota(DayOffTypeConstant.QUOTA_OF_SICK);
        } else {
            dayOffType.setQuota(DayOffTypeConstant.QUOTA_OF_PREGNANT);
        }

        dayOffType.setType(type);
        dayOffType.setRemainingTime(dayOffType.getQuota());
        return dayOffType;
    }
}