package vn.novahub.helpdesk.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import vn.novahub.helpdesk.model.Issue;

@Repository
public interface IssueRepository extends PagingAndSortingRepository<Issue, Long> {

    @Query("FROM Issue issue WHERE issue.id = :issueId AND issue.accountId = :accountId")
    Issue getByIssueIdAndAccountId(@Param("issueId") long issueId,
                                          @Param("accountId") long accountId);

    Issue getByIdAndAccountId(long issueId, long accountId);

    @Query("SELECT issue, account FROM Issue issue JOIN Account account ON issue.accountId = account.id WHERE issue.id = :issueId")
    Issue getWithAccountById(long issueId);

    Page<Issue> getAllByTitleLikeOrContentLike(String title, String content, Pageable pageable);

    @Query("SELECT issue, account FROM Issue issue JOIN Account account On issue.accountId = account.id WHERE issue.title LIKE :keyword OR issue.content LIKE :keyword")
    Page<Issue> getWithAccountByKeyWord(@Param("keyword") String keyword,
                                                Pageable pageable);

    Page<Issue> getAllByTitleLikeAndContentLikeAndStatus(String title, String content, String status, Pageable pageable);

    @Query("SELECT issue, account FROM Issue issue JOIN Account account On issue.accountId = account.id WHERE issue.status = :status AND (issue.title LIKE :keyword OR issue.content LIKE :keyword)")
    Page<Issue> getWithAccountByKeyWordAndStatus(@Param("keyword") String keyword,
                                         @Param("status") String status,
                                         Pageable pageable);

    @Query("FROM Issue issue WHERE issue.accountId = :accountId AND (issue.title LIKE :keyword OR issue.content LIKE :keyword)")
    Page<Issue> getAllByAccountIdAndKeyWord(@Param("accountId") long accountId,
                                            @Param("keyword") String keyword,
                                            Pageable pageable);

    @Query("SELECT issue, account FROM Issue issue JOIN Account account On issue.accountId = account.id WHERE issue.accountId = :accountId AND (issue.title LIKE :keyword OR issue.content LIKE :keyword)")
    Page<Issue> getAllWithAccountByAccountIdAndKeyWord(@Param("accountId") long accountId,
                                            @Param("keyword") String keyword,
                                            Pageable pageable);

    @Query("FROM Issue issue WHERE issue.status = :status AND issue.accountId = :accountId AND (issue.title LIKE :keyword OR issue.content LIKE :keyword)")
    Page<Issue> getAllByAccountIdAndKeyWordAndStatus(@Param("accountId") long accountId,
                                            @Param("keyword") String keyword,
                                            @Param("status") String status,
                                            Pageable pageable);

    @Query("SELECT issue, account FROM Issue issue JOIN Account account On issue.accountId = account.id WHERE issue.status = :status AND issue.accountId = :accountId AND (issue.title LIKE :keyword OR issue.content LIKE :keyword)")
    Page<Issue> getAllWithAccountByAccountIdAndKeyWordAndStatus(@Param("accountId") long accountId,
                                            @Param("keyword") String keyword,
                                            @Param("status") String status,
                                            Pageable pageable);

    Issue findByIdAndToken(long id, String token);

    Issue getById(long issueId);

    boolean existsByIdAndAccountId(long issueId, long accountId);

    void deleteByIdAndAccountId(long issueId, long accountId);
}
