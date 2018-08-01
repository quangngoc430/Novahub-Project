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

    Issue getByIdAndAccountId(long issueId, long accountId);

    Page<Issue> getAllByTitleContainingOrContentContaining(String title,
                                                           String content,
                                                           Pageable pageable);

    @Query("SELECT issue FROM Issue issue WHERE (issue.title LIKE CONCAT('%', :title, '%') OR issue.content LIKE CONCAT('%', :content, '%')) AND issue.status = :status")
    Page<Issue> getAllByTitleContainingOrContentContainingAndStatus(@Param("title") String title,
                                                                    @Param("content") String content,
                                                                    @Param("status") String status,
                                                                    Pageable pageable);

    @Query("FROM Issue issue " +
           "WHERE issue.accountId = :accountId " +
           "AND (issue.title LIKE CONCAT('%', :keyword, '%') " +
            "OR issue.content LIKE CONCAT('%', :keyword, '%'))")
    Page<Issue> getAllByAccountIdAndTitleContainingOrContentContaining(@Param("accountId") long accountId,
                                                                       @Param("keyword") String keyword,
                                                                       Pageable pageable);

    @Query("FROM Issue issue " +
           "WHERE issue.status = :status " +
           "AND issue.accountId = :accountId " +
           "AND (issue.title LIKE CONCAT('%', :keyword, '%') " +
            "OR issue.content LIKE CONCAT('%', :keyword, '%'))")
    Page<Issue> getAllByAccountIdAndStatusAndTitleContainingAndContentContaining(@Param("accountId") long accountId,
                                                                                 @Param("keyword") String keyword,
                                                                                 @Param("status") String status,
                                                                                 Pageable pageable);

    Issue findByIdAndToken(long id, String token);

    boolean existsByIdAndAccountId(long issueId, long accountId);

    void deleteByIdAndAccountId(long issueId, long accountId);
}
