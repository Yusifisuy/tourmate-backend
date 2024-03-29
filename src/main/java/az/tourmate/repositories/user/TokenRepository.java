package az.tourmate.repositories.user;

import az.tourmate.models.user.token.Token;
import az.tourmate.models.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface TokenRepository extends JpaRepository<Token,Long> {

    @Query("select t from Token t inner join User u on t.user.id=u.id where u.id=?1 and t.expired=false or t.revoked=false ")
    List<Token> findAllValidTokenByUser(Long id);
    List<Token> findAllByExpiredIsFalseAndRevokedIsFalseAndUser(User user);
    Optional<Token> findByToken(String token);

    void deleteAllByUserId(Long userId);
}
