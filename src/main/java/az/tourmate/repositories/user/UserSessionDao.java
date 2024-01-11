package az.tourmate.repositories.user;

import az.tourmate.models.order.Session;
import az.tourmate.models.user.UserSession;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class UserSessionDao {

    private static final String HASH_KEY = "usersession";
    private final RedisTemplate template;

    public UserSession save(UserSession userSession){
        template.opsForHash().put(HASH_KEY,userSession.getEmailId(),userSession);
        return userSession;
    }

    public List<Session> findAll(){
        return template.opsForHash().values(HASH_KEY);
    }

    public UserSession findSessionById(String id){
        return (UserSession) template.opsForHash().get(HASH_KEY,id);
    }


    public String deleteSession(String id){
        template.opsForHash().delete(HASH_KEY,id);
        return "order removed !!";
    }




}
