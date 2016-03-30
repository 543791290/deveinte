package haothink;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.haothink.dao.impl.PersionDaoImpl;
import com.haothink.po.Persion;

public class MongoTest {
	
	
	@Test
	public void testRun(){
		ApplicationContext ac = new ClassPathXmlApplicationContext("spring.xml");  
		Persion p = new Persion();
		p.setAge(21);
		p.setName("wanghao");
		p.setPassword("6532140");
		PersionDaoImpl persionDaoInpl = ac.getBean(PersionDaoImpl.class);
		//persionDaoInpl.insert(p, "col");
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("id","0012");
		System.out.println(params.get("id"));
		Persion person = persionDaoInpl.findOne(params, "col");
		System.out.println(person.getName());
	}
}
