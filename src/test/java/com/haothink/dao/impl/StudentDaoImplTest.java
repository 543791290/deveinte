package com.haothink.dao.impl;

import com.haothink.dao.StudentDao;
import com.haothink.po.Student;
import com.haothink.utils.SpringUtil;
import org.junit.Before;
import org.junit.Test;
import org.springframework.context.ApplicationContext;

import java.util.List;

/**
 * Created by wanghao on 16-4-24.
 */
public class StudentDaoImplTest {

    private ApplicationContext ac = null;
    @Before
    public void init(){
        ac = SpringUtil.getApplicationContext();


    }
    @Test
    public void insert() throws Exception {

    }

    @Test
    public void findOne() throws Exception {

    }

    @Test
    public void findAll() throws Exception {
        System.out.println(ac);
        StudentDao studentDao = (StudentDao)ac.getBean("StudentDaoImpl");
        List<Student> studentList = studentDao.findAll("students");
    }

    @Test
    public void update() throws Exception {

    }

    @Test
    public void createCollection() throws Exception {

    }

    @Test
    public void remove() throws Exception {

    }

    @Test
    public void findAll1() throws Exception {

    }

}