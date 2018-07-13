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

    @Query("FROM Account account " +
           "WHERE account.email LIKE :keyword or account.firstName LIKE :keyword or account.lastName LIKE :keyword")
    Page<Account> getAllByEmailLikeOrFirstNameLikeOrLastNameLike(@Param("keyword") String keyword, Pageable pageable);

    @Query("FROM Account account " +
           "WHERE (account.email LIKE :keyword or account.firstName LIKE :keyword or account.lastName LIKE :keyword) " +
           "AND account.status = :status")
    Page<Account> getAllByEmailLikeOrFirstNameLikeOrLastNameLikeAndStatus(@Param("keyword") String keyword,
                                                                          @Param("status") String status, Pageable pageable);

    @Query("SELECT account " +
           "FROM Account account " +
           "JOIN Role role " +
           "ON account.roleId = role.id " +
           "WHERE (account.email LIKE :keyword or account.firstName LIKE :keyword or account.lastName LIKE :keyword) " +
           "AND role.name = :name")
    Page<Account> getAllByEmailLikeOrFirstNameLikeOrLastNameLikeAndRole(@Param("keyword") String keyword,
                                                                        @Param("name") String name,
                                                                        Pageable pageable);

    @Query("SELECT account " +
            "FROM Account account " +
            "JOIN Role role " +
            "ON account.roleId = role.id " +
            "WHERE (account.email LIKE :keyword or account.firstName LIKE :keyword or account.lastName LIKE :keyword) " +
            "AND account.status = :status " +
            "AND role.name = :name")
    Page<Account> getAllByEmailLikeOrFirstNameLikeOrLastNameLikeAndStatusAndRole(@Param("keyword") String keyword,
                                                                                 @Param("status") String status,
                                                                                 @Param("name") String name,
                                                                                 Pageable pageable);

    Account getById(long accountId);

    Account getByIdAndVertificationToken(long accountId, String verificationToken);

    Account getByEmailAndPassword(String email, String password);

    @Query("SELECT account FROM Account account JOIN Role role ON account.roleId = role.id WHERE role.name = :name")
    List<Account> getAllByRoleName(@Param("name") String roleName);

    @Query("SELECT account " +
           "FROM Account account " +
           "JOIN AccountHasSkill accountHasSkill " +
           "ON account.id = accountHasSkill.accountId " +
           "WHERE accountHasSkill.skillId = :skillId")
    Page<Account> getAllBySkillId(@Param("skillId") long skillId, Pageable pageable);
}
