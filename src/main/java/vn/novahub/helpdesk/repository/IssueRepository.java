package vn.novahub.helpdesk.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import vn.novahub.helpdesk.model.Issue;

import java.util.List;

@Repository
public interface IssueRepository extends PagingAndSortingRepository<Issue, Long> {

    @Query("FROM Issue issue WHERE issue.id = :issueId AND issue.accountId = :accountId")
    Issue getAnIssueByIssueIdAndAccountId(@Param("issueId") long issueId, 
                                          @Param("accountId") long accountId);

    @Query("SELECT issue, account FROM Issue issue JOIN Account account ON issue.accountId = account.id WHERE issue.id = :issueId AND issue.accountId = :accountId")
    Issue getAnIssueWithAccountByIssueIdAndAccountId(@Param("issueId") long issueId, 
                                          @Param("accountId") long accountId);

    @Query("SELECT issue, account FROM Issue issue JOIN Account account ON issue.accountId = account.id WHERE issue.id = :issueId")
    Issue getAnIssueWithAccountById(long issueId);

    @Query("FROM Issue issue WHERE issue.title LIKE :keyword OR issue.content LIKE :keyword")
    Page<Issue> getAllIssuesByKeyWord(@Param("keyword") String keyword,
                                      Pageable pageable);

    @Query("SELECT issue, account FROM Issue issue JOIN Account account On issue.accountId = account.id WHERE issue.title LIKE :keyword OR issue.content LIKE :keyword")
    Page<Issue> getAllIssuesWithAccountByKeyWord(@Param("keyword") String keyword,
                                                Pageable pageable);

    @Query("FROM Issue issue WHERE issue.status = :status AND (issue.title LIKE :keyword OR issue.content LIKE :keyword)")
    Page<Issue> getAllIssuesByKeyWordAndStatus(@Param("keyword") String keyword,
                                               @Param("status") String status,
                                               Pageable pageable);

    @Query("SELECT issue, account FROM Issue issue JOIN Account account On issue.accountId = account.id WHERE issue.status = :status AND (issue.title LIKE :keyword OR issue.content LIKE :keyword)")
    Page<Issue> getAllIssuesWithAccountByKeyWordAndStatus(@Param("keyword") String keyword,
                                         @Param("status") String status,
                                         Pageable pageable);

    @Query("FROM Issue issue WHERE issue.accountId = :accountId AND (issue.title LIKE :keyword OR issue.content LIKE :keyword)")
    Page<Issue> getAllIssuesByAccountIdAndKeyWord(@Param("accountId") long accountId,
                                            @Param("keyword") String keyword,
                                            Pageable pageable);

    @Query("SELECT issue, account FROM Issue issue JOIN Account account On issue.accountId = account.id WHERE issue.accountId = :accountId AND (issue.title LIKE :keyword OR issue.content LIKE :keyword)")
    Page<Issue> getAllIssuesWithAccountByAccountIdAndKeyWord(@Param("accountId") long accountId,
                                            @Param("keyword") String keyword,
                                            Pageable pageable);

    @Query("FROM Issue issue WHERE issue.status = :status AND issue.accountId = :accountId AND (issue.title LIKE :keyword OR issue.content LIKE :keyword)")
    Page<Issue> getAllIssuesByAccountIdAndKeyWordAndStatus(@Param("accountId") long accountId,
                                            @Param("keyword") String keyword,
                                            @Param("status") String status,
                                            Pageable pageable);

    @Query("SELECT issue, account FROM Issue issue JOIN Account account On issue.accountId = account.id WHERE issue.status = :status AND issue.accountId = :accountId AND (issue.title LIKE :keyword OR issue.content LIKE :keyword)")
    Page<Issue> getAllIssuesWithAccountByAccountIdAndKeyWordAndStatus(@Param("accountId") long accountId,
                                            @Param("keyword") String keyword,
                                            @Param("status") String status,
                                            Pageable pageable);
}
