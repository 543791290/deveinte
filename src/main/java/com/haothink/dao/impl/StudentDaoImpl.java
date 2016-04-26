package com.haothink.dao.impl;

import com.haothink.dao.StudentDao;
import com.haothink.po.Student;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * Created by wanghao on 16-4-24.
 */
@Repository
public class StudentDaoImpl implements StudentDao{

    @Autowired
    private MongoTemplate mongoTemplate;

    public void insert(Student student, String collectionName) {

        mongoTemplate.insert(student,collectionName);
    }

    public Student findOne(Map<String, Object> params, String collectionName) {
        return mongoTemplate.findOne(new Query(Criteria.where("id").is(params.get("id"))), Student.class, collectionName);
    }

    public List<Student> findAll(Map<String, Object> params, String collectionName) {

        List<Student> result = mongoTemplate.find(new Query(Criteria.where("age").lt(params.get("maxAge"))), Student.class,collectionName);
        return result;
    }


    public void update(Map<String, Object> params, String collectionName) {

        mongoTemplate.upsert(new Query(Criteria.where("id").is(params.get("id"))), new Update().set("name", params.get("name")), Student.class,collectionName);
    }


    public void createCollection(String collectionName) {

        mongoTemplate.createCollection(collectionName);
    }


    public void remove(Map<String, Object> params, String collectionName) {

        mongoTemplate.remove(new Query(Criteria.where("id").is(params.get("id"))),Student.class,collectionName);
    }

    public List<Student> findAll(String collectionName) {

        return mongoTemplate.findAll(Student.class, collectionName);
    }
}
