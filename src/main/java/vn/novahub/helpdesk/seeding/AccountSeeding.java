package vn.novahub.helpdesk.seeding;

import com.github.javafaker.Faker;
import com.github.javafaker.Name;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;
import vn.novahub.helpdesk.enums.AccountEnum;
import vn.novahub.helpdesk.enums.RoleEnum;
import vn.novahub.helpdesk.model.Account;
import vn.novahub.helpdesk.repository.AccountRepository;
import vn.novahub.helpdesk.repository.RoleRepository;

import java.util.ArrayList;
import java.util.Date;

@Component
public class AccountSeeding {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    public ArrayList<Account> generateData(long numberOfAccounts) {
        ArrayList<Account> accountArrayList = new ArrayList<>();

        Faker faker = new Faker();

        for(int i = 0; i < numberOfAccounts; i++) {
            String firstName = faker.name().firstName();
            String lastName = faker.name().lastName();
            Account account = new Account();
            account.setEmail(firstName.toLowerCase() + "." + lastName.toLowerCase() + "@novahub.vn");
            account.setFirstName(firstName);
            account.setLastName(lastName);
            account.setPassword(bCryptPasswordEncoder.encode("password"));
            account.setStatus(AccountEnum.ACTIVE.name());
            account.setAddress(faker.address().fullAddress());
            Date dayOfBirth = faker.date().birthday();
            account.setDayOfBirth(new Date(dayOfBirth.getYear(), dayOfBirth.getMonth(), dayOfBirth.getDay()));
            account.setRoleId(roleRepository.getByName(RoleEnum.USER.name()).getId());
            account.setCreatedAt(new Date());
            account.setUpdatedAt(new Date());

            accountArrayList.add(accountRepository.save(account));
        }

        return accountArrayList;
    }
}
