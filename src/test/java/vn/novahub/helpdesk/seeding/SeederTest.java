package vn.novahub.helpdesk.seeding;

import com.fasterxml.jackson.core.type.TypeReference;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import vn.novahub.helpdesk.model.DayOff;
import vn.novahub.helpdesk.model.DayOffType;

import java.util.List;

import static org.junit.Assert.*;

public class SeederTest {
    Logger logger = LoggerFactory.getLogger(this.getClass());

    @Test
    public void generateDayOffType() throws Exception {
        Seeder seeder = new Seeder();
        List<DayOffType> dayOffTypeList
                = seeder.generate("seeding/day_off_type.json", new TypeReference<List<DayOffType>>() {});
        logger.info(dayOffTypeList.get(0).getType());
        logger.info(dayOffTypeList.get(1).getType());
        logger.info(dayOffTypeList.get(1).getDefaultQuota()+"");
    }



}