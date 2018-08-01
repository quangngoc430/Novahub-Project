package vn.novahub.helpdesk.service;

import vn.novahub.helpdesk.exception.CommonTypeIsNotExistException;
import vn.novahub.helpdesk.model.CommonDayOffType;

import java.util.List;

public interface CommonDayOffTypeService {

    List<CommonDayOffType> getAllCommonType();

    CommonDayOffType create(CommonDayOffType commonDayOffType);

    CommonDayOffType update(CommonDayOffType commonDayOffType) throws CommonTypeIsNotExistException;

    CommonDayOffType delete(int commonTypeId) throws CommonTypeIsNotExistException;
}
