package com.haothink.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.haothink.mapper.UserMapper;
import com.haothink.po.User;
import com.haothink.service.UserService;

@Service
public class UserServiceImpl implements UserService{
	
	@Autowired
	private UserMapper userMapper;
	
	public User getUserById(int id) {
		
		return userMapper.selectByPrimaryKey(id);
	}
	
	
}
