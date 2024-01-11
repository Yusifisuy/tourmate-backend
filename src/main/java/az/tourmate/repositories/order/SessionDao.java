package az.tourmate.repositories.order;

import az.tourmate.models.order.Session;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class SessionDao {

    private static final String HASH_KEY = "Session";
    private final RedisTemplate template;


    public Session save(Session session){
        template.opsForHash().put(HASH_KEY,session.getId(),session);
        return session;
    }

    public List<Session> findAll(){
        return template.opsForHash().values(HASH_KEY);
    }



    public Session findSessionById(Long id){
        return (Session) template.opsForHash().get(HASH_KEY,id);
    }


    public String deleteSession(Long id){
        template.opsForHash().delete(HASH_KEY,id);
        return "order removed !!";
    }
}
