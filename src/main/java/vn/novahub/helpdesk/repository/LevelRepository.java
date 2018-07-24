package vn.novahub.helpdesk.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import vn.novahub.helpdesk.model.Level;

@Repository
public interface LevelRepository extends CrudRepository<Level, Long> {

    boolean existsByValueAndAccountIdAndSkillId(int value, long accountId, long skillId);

    Level getByAccountIdAndSkillId(long accountId, long skillId);
}
