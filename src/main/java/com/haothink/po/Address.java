package com.haothink.po;

import java.io.Serializable;

import org.springframework.data.mongodb.core.mapping.Document;

@Document
public class Address implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 3206116262377882409L;
	
	private Long id;
	private String loc;
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getLoc() {
		return loc;
	}
	public void setLoc(String loc) {
		this.loc = loc;
	}
	
	
	
}
