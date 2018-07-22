package vn.novahub.helpdesk.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import vn.novahub.helpdesk.model.Token;

@Repository
public interface TokenRepository extends CrudRepository<Token, Long> {

    Token getByAccessToken(String accessToken);

}
