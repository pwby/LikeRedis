package Function;

import org.junit.Test;
import redis.clients.jedis.Jedis;

import java.util.List;

public class FunctionTest {
    Jedis client=new Jedis();
    String key="key";
    String value="test";
    @Test
    public void testLPush() {
        long result = client.lpush(key, value);
        System.out.println("列表插入成功返回列表中的数目" + result);
        result=client.lpush(key,value);
        System.out.println("列表插入成功返回列表中的数目" + result);
    }

    @Test
    public void testLRange() {
        //多组测试数据
        //第一组：开始值(start)和停止值(end)都未越界，预期结果：返回开始值至停止值间的数据（包含开始值和停止值所在下标）
        List<String> result = client.lrange(key, 0, 0);
        System.out.println(result);

        //第二组：开始值(start)未越界，停止值超过列表长度，预期结果：返回开始值至列表末尾的所有数据
         result = client.lrange(key, 0, 10);
         System.out.println(result);

        //第三组：开始值(start)未越界，停止值为负数 预期结果：返回停止值所在下标（下标为负，例如-n，代表从列表数据末尾的第一个数数至第n个数所在的下标）至数据末尾的所有数据
         result = client.lrange(key, 0, -2);
        System.out.println(result);

        //第四组：开始值(start)越界，停止值未越界,预期结果：不会有结果输出
        result = client.lrange(key, 0, 0);
        System.out.println(result);

        //第五组：开始值(start)为负，停止值未越界,预期结果：如第三组所示
        result = client.lrange(key, -2, 0);
        System.out.println(result);
    }

    @Test
    public void hset(){
        long result=client.hset(key,key,value);
        System.out.println("列表插入成功返回1:" + result);

        result=client.hset(key,key,value);
        System.out.println("列表覆盖成功0:" + result);
    }

    @Test
    public void hget(){
       //根据key值取出value
        String value=client.hget(key,key);
        System.out.println("key 对应的value值为:"+value);

    }

    @Test
    public void saad(){
        //所插入的元素不会重复
       long result= client.sadd(key,value);
        System.out.println("插入成功返回1:"+result);

        result= client.sadd(value,value);
        System.out.println("插入成功返回1:"+result);

        result=  client.sadd(value,key);
        System.out.println("覆盖成功返回0:"+result);

    }


}
