package vn.novahub.helpdesk.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import vn.novahub.helpdesk.model.Level;

import javax.transaction.Transactional;
import java.util.List;

@Repository
@Transactional
public interface LevelRepository extends PagingAndSortingRepository<Level, Long> {

    boolean existsByValueAndAccountIdAndSkillId(long value, long accountId, long skillId);

    Level getByAccountIdAndSkillId(long accountId, long skillId);

    void deleteByAccountIdAndSkillId(long accountId, long skillId);

    Level getBySkillId(long skillId);

    Page<Level> getAllByAccountId(long accountId, Pageable pageable);

    List<Level> getAllByAccountId(long accountId);

    List<Level> getAllByAccountIdIn(List<Long> accountIds);
}
