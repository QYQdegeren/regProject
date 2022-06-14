import com.reggie.ReggieApplication;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

@SpringBootTest(classes = ReggieApplication.class)
public class redistest {

    @Autowired
    private RedisTemplate redisTemplate;

    @Test
    void te(){
        ValueOperations valueOperations = redisTemplate.opsForValue();
        valueOperations.set("aa","123");
    }
}
