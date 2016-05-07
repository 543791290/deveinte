package com.haothink.controller;

import com.haothink.dao.StudentDao;
import com.haothink.po.Student;
import com.haothink.po.User;
import com.haothink.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
public class UserController {
	
	@Autowired
	private UserService userService;
	@Autowired
	private StudentDao studentDao;

	@RequestMapping("/findUser")
	public String getUser(Model model,Integer id){
		User user = userService.getUserById(id);
		System.out.println(user);
		model.addAttribute("user", user);
		return "/index";
	}

	@RequestMapping("/findStudents")
	public @ResponseBody
	List<Student> findStudents(){
		return studentDao.findAll("students");
	}
}
