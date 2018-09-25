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

    @Query("FROM Issue issue " +
           "WHERE (lower(issue.title) LIKE CONCAT('%', lower(trim(:title)), '%') " +
           "OR lower(issue.content) LIKE CONCAT('%', lower(trim(:content)), '%'))")
    Page<Issue> getAllByTitleContainingOrContentContaining(@Param("title") String title,
                                                           @Param("content") String content,
                                                           Pageable pageable);

    @Query("FROM Issue issue " +
           "WHERE (lower(issue.title) LIKE CONCAT('%', lower(trim(:title)), '%') " +
           "OR lower(issue.content) LIKE CONCAT('%', lower(trim(:content)), '%')) " +
           "AND issue.status = :status")
    Page<Issue> getAllByTitleContainingOrContentContainingAndStatus(@Param("title") String title,
                                                                    @Param("content") String content,
                                                                    @Param("status") String status,
                                                                    Pageable pageable);

    @Query("FROM Issue issue " +
           "WHERE issue.accountId = :accountId " +
           "AND (lower(issue.title) LIKE CONCAT('%', lower(trim(:keyword)), '%') " +
           "OR lower(issue.content) LIKE CONCAT('%', lower(trim(:keyword)), '%')) " +
           "AND issue.status <> 'CANCELLED'")
    Page<Issue> getAllByAccountIdAndTitleContainingOrContentContaining(@Param("accountId") long accountId,
                                                                       @Param("keyword") String keyword,
                                                                       Pageable pageable);

    @Query("FROM Issue issue " +
           "WHERE issue.status = :status " +
           "AND issue.status <> 'CANCELLED' " +
           "AND issue.accountId = :accountId " +
           "AND (lower(issue.title) LIKE CONCAT('%', lower(trim(:keyword)), '%') " +
           "OR lower(issue.content) LIKE CONCAT('%', lower(trim(:keyword)), '%'))")
    Page<Issue> getAllByAccountIdAndStatusAndTitleContainingAndContentContaining(@Param("accountId") long accountId,
                                                                                 @Param("keyword") String keyword,
                                                                                 @Param("status") String status,
                                                                                 Pageable pageable);

    Issue getByIdAndStatusIsNot(long issueId, String status);

    Issue getByIdAndAccountIdAndStatusIsNot(long issueId, long accountId, String status);
}
