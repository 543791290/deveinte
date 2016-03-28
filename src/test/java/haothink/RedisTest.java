package haothink;

import org.junit.Test;

import com.haothink.po.Persion;
import com.haothink.utils.RedisUtil;

public class RedisTest {
	
	@Test
	public void save(){
		Persion p = new Persion();
		p.setAge(21);
		p.setId("0012");
		p.setName("wanghao");
		p.setPassword("6532140");
		RedisUtil.set("wanghao", "wanghao");
	}
}
