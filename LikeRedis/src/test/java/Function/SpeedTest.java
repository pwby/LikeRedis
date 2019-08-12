package Function;

import org.junit.Test;
import redis.clients.jedis.Jedis;

public class SpeedTest {
   @Test
    public void  avgSpeed(){
       Jedis client = new Jedis();
       long b = System.nanoTime();
       for (int i = 0; i < 10000; i++) {
           String str=String.valueOf(i);
           client.lpush(str,str);
           client.lrange(str,0,0);
           client.lpop(str);
       }
       long e = System.nanoTime();
       System.out.println(e-b);
    }
}
