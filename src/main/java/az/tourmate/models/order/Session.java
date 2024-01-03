package az.tourmate.models.order;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.redis.core.RedisHash;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@RedisHash("Session")
public class Session implements Serializable {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Order order;

}
