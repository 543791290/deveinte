package haothink;

import java.util.Date;

import org.junit.Test;

import com.haothink.po.Persion;
import com.haothink.utils.RedisUtil;

import redis.clients.jedis.Jedis;

public class RedisTest {
	
	@Test
	public void save(){
	
	}
	@Test
	public void testRun(){
		   Jedis jedis = new Jedis("localhost");
		   
		      System.out.println("Connection to server sucessfully");
		      //设置 redis 字符串数据
		      jedis.set("w3ckey", "Redis tutorial");
		     // 获取存储的数据并输出
		     System.out.println("Stored string in redis:: "+ jedis.get("w3ckey"));
	}
	
	public static void main(String[] args) {
		Persion p = new Persion();
		p.setAge(21);
		p.setName("wanghao");
		p.setPassword("6532140");
		RedisUtil.set("hellow", new Date()+"");
    	String name = RedisUtil.get("hewllo");
    	if(RedisUtil.exists("hellow")){
    		System.out.println(RedisUtil.get("wanghao"));
    	}
		System.out.println(name);
	}
}
