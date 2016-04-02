package com.haothink.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.haothink.po.User;
import com.haothink.service.UserService;

@Controller
public class UserController {
	
	@Autowired
	private UserService userService;
	
	@RequestMapping("/findUser")
	public String getUser(Model model,Integer id){
		User user = userService.getUserById(id);
		System.out.println(user);
		model.addAttribute("user", user);
		return "/index";
	}
}
