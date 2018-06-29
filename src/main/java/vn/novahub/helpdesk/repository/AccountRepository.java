package vn.novahub.helpdesk.repository;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import vn.novahub.helpdesk.model.Account;

@Repository
public interface AccountRepository extends PagingAndSortingRepository<Account, Long> {

    Account findByEmailAndPassword(String email, String password);

}
