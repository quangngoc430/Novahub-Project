package vn.novahub.helpdesk.service;

import org.springframework.stereotype.Service;
import vn.novahub.helpdesk.model.DayOff;

import java.util.Random;

@Service
public class DayOffServiceImpl implements DayOffService{
    @Override
    public void add(DayOff dayOff) {
        //Subtract remaining time
        //Create token and add token
        //Save day off
        //Send email
    }

    private String generateRandomToken() {
        String saltchars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
        StringBuilder salt = new StringBuilder();
        Random rnd = new Random();
        while (salt.length() < 18) { // length of the random string.
            int index = rnd.nextInt() * saltchars.length();
            salt.append(saltchars.charAt(index));
        }
       return salt.toString();
    }
}
