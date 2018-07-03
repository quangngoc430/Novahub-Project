package vn.novahub.helpdesk.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import vn.novahub.helpdesk.model.Account;
import vn.novahub.helpdesk.repository.AccountRepository;

@Service
public class MyUserDetailService implements UserDetailsService {

    @Autowired
    private AccountRepository accountRepository;

    @Override
    public UserDetails loadUserByUsername(final String username) throws UsernameNotFoundException{

        Account account = accountRepository.getByEmail(username);

        if(account == null)
            throw new UsernameNotFoundException("Username Not Found");

        boolean enabled = true;
        boolean accountNonExpired = true;
        boolean credentialsNonExpired = true;
        boolean accountNonLocked = true;

        return new User(username, account.getPassword(), enabled, accountNonExpired, credentialsNonExpired, accountNonLocked, account.getAuthorities());
    }
}
