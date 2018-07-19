package vn.novahub.helpdesk.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import vn.novahub.helpdesk.model.Account;

import java.util.List;

@Repository
public interface AccountRepository extends PagingAndSortingRepository<Account, Long> {

    Account findByEmailAndPassword(String email, String password);

    Account getByEmail(String email);

    Page<Account> getAllByEmailLikeAndFirstNameLikeAndLastNameLike(String email, String firstName, String lastName, Pageable pageable);

    Account getById(long accountId);

    Account getByIdAndVertificationToken(long accountId, String verificationToken);

    Account getByEmailAndPassword(String email, String password);

    @Query("SELECT account FROM Account account JOIN Role role ON account.roleId = role.id WHERE role.name = :name")
    List<Account> getAllByRoleName(@Param("name") String roleName);
}
