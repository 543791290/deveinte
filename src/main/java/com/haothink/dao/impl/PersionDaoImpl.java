package com.haothink.dao.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

import com.haothink.dao.PersionDao;
import com.haothink.po.Persion;

@Repository
public class PersionDaoImpl implements PersionDao{

	@Autowired
	private MongoTemplate mongoTemplate;  


	public void insert(Persion object, String collectionName) {

		mongoTemplate.insert(object, collectionName);  
	}


	public Persion findOne(Map<String, Object> params, String collectionName) {

		return mongoTemplate.findOne(new Query(Criteria.where("id").is(params.get("id"))), Persion.class, collectionName);
	}


	public List<Persion> findAll(Map<String, Object> params, String collectionName) {

		List<Persion> result = mongoTemplate.find(new Query(Criteria.where("age").lt(params.get("maxAge"))), Persion.class,collectionName);  
		return result;  
	}


	public void update(Map<String, Object> params, String collectionName) {
		
		 mongoTemplate.upsert(new Query(Criteria.where("id").is(params.get("id"))), new Update().set("name", params.get("name")), Persion.class,collectionName);  
	}


	public void createCollection(String collectionName) {
	
		mongoTemplate.createCollection(collectionName);  
	}


	public void remove(Map<String, Object> params, String collectionName) {
		// TODO Auto-generated method stub
		mongoTemplate.remove(new Query(Criteria.where("id").is(params.get("id"))),Persion.class,collectionName);  
	}

}
