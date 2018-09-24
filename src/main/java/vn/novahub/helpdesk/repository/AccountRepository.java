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

    Account getByEmail(String email);

    @Query( "SELECT account " +
            "FROM Account account " +
            "WHERE lower(account.email) LIKE CONCAT('%', lower(trim(:keyword)), '%') " +
            "OR lower(account.firstName) LIKE CONCAT('%', lower(trim(:keyword)), '%') " +
            "OR lower(account.lastName) LIKE CONCAT('%' , lower(trim(:keyword)), '%')")
    Page<Account> getAllByEmailContainingOrFirstNameContainingOrLastNameContaining(@Param("keyword") String keyword, Pageable pageable);

    @Query( "SELECT account " +
            "FROM Account account " +
            "WHERE (lower(account.email) LIKE CONCAT('%', lower(trim(:keyword)), '%') " +
            "OR lower(account.firstName) LIKE CONCAT('%', lower(trim(:keyword)), '%') " +
            "OR lower(account.lastName) LIKE CONCAT('%' , lower(trim(:keyword)), '%')) " +
            "AND account.status = :status")
    Page<Account> getAllByEmailContainingOrFirstNameContainingOrLastNameContainingAndStatus(@Param("keyword") String keyword,
                                                                                            @Param("status") String status,
                                                                                            Pageable pageable);

    @Query("SELECT account " +
           "FROM Account account " +
           "JOIN Role role " +
           "ON account.roleId = role.id " +
           "WHERE (lower(account.email) LIKE CONCAT('%', lower(trim(:keyword)), '%') " +
           "OR lower(account.firstName) LIKE CONCAT('%', lower(trim(:keyword)), '%') " +
           "OR lower(account.lastName) LIKE CONCAT('%', lower(trim(:keyword)), '%')) " +
           "AND role.name = :role")
    Page<Account> getAllByEmailContainingOrFirstNameContainingOrLastNameContainingAndRole(@Param("keyword") String keyword,
                                                                                          @Param("role") String role,
                                                                                          Pageable pageable);

    @Query("SELECT account " +
            "FROM Account account " +
            "JOIN Role role " +
            "ON account.roleId = role.id " +
            "WHERE (lower(account.email) LIKE CONCAT('%', lower(trim(:keyword)), '%') " +
            "OR lower(account.firstName) LIKE CONCAT('%', lower(trim(:keyword)), '%') " +
            "OR lower(account.lastName) LIKE CONCAT('%', lower(trim(:keyword)), '%')) " +
            "AND account.status = :status " +
            "AND role.name = :role")
    Page<Account> getAllByEmailContainingOrFirstNameContainingOrLastNameContainingAndStatusAndRole(@Param("keyword") String keyword,
                                                                                                   @Param("status") String status,
                                                                                                   @Param("role") String role,
                                                                                                   Pageable pageable);

    Account getByIdAndVerificationToken(long accountId, String verificationToken);

    @Query("SELECT account " +
           "FROM Account account " +
           "JOIN Role role " +
           "ON account.roleId = role.id " +
           "WHERE role.name = :role")
    List<Account> getAllByRoleName(@Param("role") String role);

    @Query("SELECT account " +
           "FROM Account account " +
           "JOIN AccountHasSkill accountHasSkill " +
           "ON account.id = accountHasSkill.accountId " +
           "WHERE accountHasSkill.skillId = :skillId")
    Page<Account> getAllBySkillId(@Param("skillId") long skillId, Pageable pageable);

    @Query("SELECT account " +
           "FROM Account account " +
           "WHERE 0 < (SELECT count(accountHasSkill) " +
                      "FROM AccountHasSkill accountHasSkill " +
                      "WHERE accountHasSkill.skillId IN :skillIds AND accountHasSkill.accountId = account.id)")
    Page<Account> getAllBySkillIdIsIn(@Param("skillIds") List<Long> skillIds, Pageable pageable);
}
