package Function;

import org.junit.Test;
import redis.clients.jedis.Jedis;

public class MulClientTest {
    @Test
    public void mulClient(){

            for (int i = 0; i < 20000; i++) {
                Jedis client = new Jedis();
                client.lpush("test", +i + "");
            }

    }
}
