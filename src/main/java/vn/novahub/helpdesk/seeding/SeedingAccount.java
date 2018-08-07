package vn.novahub.helpdesk.seeding;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;
import vn.novahub.helpdesk.model.Account;
import vn.novahub.helpdesk.repository.AccountRepository;
import vn.novahub.helpdesk.repository.RoleRepository;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

@Component
public class SeedingAccount {

    @Autowired
    private ResourceLoader resourceLoader;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    public ArrayList<Account> generateData(String fileName) throws IOException, ParseException {
        ArrayList<Account> accountArrayList = new ArrayList<>();

        ObjectMapper objectMapper = new ObjectMapper();
        Resource res = resourceLoader.getResource("classpath:" + fileName);
        JsonNode jsonNodeRoot = objectMapper.readValue(res.getFile(), JsonNode.class);
        JsonNode jsonNodeAccount = jsonNodeRoot.get("account");

        for(int i = 0; i < jsonNodeAccount.size(); i++) {
            JsonNode currentJsonNode = jsonNodeAccount.get(i);
            String email = currentJsonNode.get("email").textValue();

            Account account = accountRepository.getByEmail(email);

            if(account == null) {
                account = new Account();
                account.setEmail(email);
                account.setPassword(bCryptPasswordEncoder.encode(currentJsonNode.get("password").textValue()));
                account.setFirstName(currentJsonNode.get("firstname").textValue());
                account.setLastName(currentJsonNode.get("lastname").textValue());
                account.setAddress(currentJsonNode.get("address").textValue());
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                account.setDayOfBirth(format.parse(currentJsonNode.get("dayOfBirth").textValue()));
                account.setJoiningDate(format.parse(currentJsonNode.get("joiningDate").textValue()));
                account.setStatus(currentJsonNode.get("status").textValue());
                account.setRoleId(roleRepository.getByName(currentJsonNode.get("role").textValue()).getId());
                account.setCreatedAt(new Date());
                account.setUpdatedAt(new Date());

                account = accountRepository.save(account);
            }

            accountArrayList.add(account);
        }

        return accountArrayList;
    }
}