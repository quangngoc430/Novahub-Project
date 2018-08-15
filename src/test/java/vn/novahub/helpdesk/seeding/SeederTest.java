package vn.novahub.helpdesk.seeding;

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
                = seeder.generate("seeding/day_off_type.json", DayOffType.class);
        logger.info(dayOffTypeList.get(0).getType());
        logger.info(dayOffTypeList.get(1).getType());
        logger.info(dayOffTypeList.get(1).getDefaultQuota()+"");
    }

    @Test
    public void generateDayOff() throws Exception {
        Seeder seeder = new Seeder();
        List<DayOff> dayOffs
                = seeder.generate("seeding/day_off.json", DayOff.class);

        logger.info(dayOffs.get(1).getCreatedAt().toString());
    }

}