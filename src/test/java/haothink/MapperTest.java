package haothink;


import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.haothink.po.User;
import com.haothink.service.UserService;

//表示继承了SpringJUnit4ClassRunner类  
@RunWith(SpringJUnit4ClassRunner.class)     
@ContextConfiguration(locations = {"classpath:spring-mybatis.xml"})  
public class MapperTest {
	private ClassPathXmlApplicationContext ac = null;  
	@Autowired
	private UserService userService;
	@Before  
	public void before() {  
		ac = new ClassPathXmlApplicationContext("spring.xml");  
		userService = (UserService) ac.getBean("userService");  
	} 
	@Test
	public void testOB(){
		User user = userService.getUserById(10);  
		System.out.println(user);
	}
}
