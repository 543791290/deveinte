package com.haothink.dao;

import java.util.List;
import java.util.Map;

/**
 *
 * @param <T>
 */
public interface MongoBase<T> {
	
	public void insert(T t, String collectionName) ;
	
	public T findOne(Map<String, Object> params, String collectionName);
	
	public List<T> findAll(Map<String, Object> params, String collectionName);
	
	public List<T> findAll(String collectionName);
	
	public void update(Map<String, Object> params, String collectionName);
	
	public void createCollection(String collectionName) ;
	
	public void remove(Map<String, Object> params, String collectionName);
	
	
	
	
}
