package vn.novahub.helpdesk.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import vn.novahub.helpdesk.model.Account;

@Repository
public interface AccountRepository extends PagingAndSortingRepository<Account, Long> {

    Account findByEmailAndPassword(String email, String password);

    Account getByEmail(String email);

    Page<Account> getAllByEmailLikeAndFirstNameLikeAndLastNameLike(String email, String firstName, String lastName, Pageable pageable);

    Account getById(long accountId);

    Account getByIdAndVertificationToken(long accountId, String verificationToken);

    Account getByEmailAndPassword(String email, String password);
}
