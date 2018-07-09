package vn.novahub.helpdesk.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import vn.novahub.helpdesk.model.DayOff;
import vn.novahub.helpdesk.model.DayOffType;
import vn.novahub.helpdesk.repository.DayOffRepository;
import vn.novahub.helpdesk.repository.DayOffTypeRepository;

import java.util.Random;

@Service
public class DayOffServiceImpl implements DayOffService{

    @Autowired
    private DayOffRepository dayOffRepository;

    @Autowired
    private DayOffTypeRepository dayOffTypeRepository;

    Logger logger = LoggerFactory.getLogger(this.getClass());

    @Override
    public void add(DayOff dayOff) {

        setDayOffType(dayOff);

        subtractRemainingTime(dayOff);

        addToken(dayOff);

        saveDayOffToDatabase(dayOff);

        sendDayOffRequest(dayOff);
    }


    private String generateRandomToken() {
        String saltchars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
//        StringBuilder salt = new StringBuilder();
//        Random rnd = new Random();
//        while (salt.length() < 18) { // length of the random string.
//            int index = rnd.nextInt() * saltchars.length();
//            salt.append(saltchars.charAt(index));
//        }
//       return salt.toString();
        return saltchars;
    }

    private void setDayOffType(DayOff dayOff) {
        DayOffType dayOffType = dayOffTypeRepository
                .findByAccountIdAndType(
                        dayOff.getAccountId(),
                        dayOff.getType());
        dayOff.setDayOffType(dayOffType);
    }

    private void subtractRemainingTime(DayOff dayOff) {
        dayOff.getDayOffType().subtractRemainingTime(dayOff.getNumberOfHours());
    }

    private void addToken(DayOff dayOff) {
        dayOff.setToken(generateRandomToken());
    }

    private void saveDayOffToDatabase(DayOff dayOff) {
        dayOffRepository.save(dayOff);
    }

    private void sendDayOffRequest(DayOff dayOff) {
        //Send email
    }
}
