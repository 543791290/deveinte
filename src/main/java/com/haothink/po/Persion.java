package com.haothink.po;

import java.io.Serializable;
import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
/**
 * 
 * @author wanghao
 * http://docs.spring.io/spring-data/mongodb/docs/current/reference/html/
 */
@Document
public class Persion implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1111490916808059129L;

	@Id
	private ObjectId id;
	@Indexed(unique = true)
	private String ssn;  
	private String name;  
	private int age;  
	private String password;
	@DBRef
	private List<Address> address;
	
	public ObjectId getId() {
		return id;
	}
	public void setId(ObjectId id) {
		this.id = id;
	}
	public String getSsn() {
		return ssn;
	}
	public void setSsn(String ssn) {
		this.ssn = ssn;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getAge() {
		return age;
	}
	public void setAge(int age) {
		this.age = age;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public List<Address> getAddress() {
		return address;
	}
	public void setAddress(List<Address> address) {
		this.address = address;
	} 
	
	

}
