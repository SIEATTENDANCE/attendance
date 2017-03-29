package com.sie.attend.controller;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.http.HttpRequest;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sie.attend.common.bo.CommonBO;
import com.sie.attend.pojo.User;

@RestController
@RequestMapping("/test")
public class TestController {
	@Resource
	private CommonBO commonBo;
	
	@RequestMapping(value = { "/find/{id}" }, method = {
			org.springframework.web.bind.annotation.RequestMethod.GET }, produces = { "application/json" })
	public String find(@PathVariable("id") String id) {
		System.out.println(id);
		Map<String, Object> m = new HashMap<String, Object>();
		m.put("id",  id);
		User user = this.commonBo.selectUser("com.sie.attend.pojo.userMapper.selectUser", m);
		System.out.println(user);
		return "test";
	}
	
	@RequestMapping("tologin")
	public String test(){
		System.out.println("登录页面");
		return "forward:/WEB-INF/html/login.html";
	}
	
	@RequestMapping("login")
	public String login(HttpServletRequest request){
		System.out.println("1111111");
		String name = request.getParameter("name");
		String pwd = request.getParameter("pwd");
		if("test".equals(name) && "test".equals(pwd)){
			return "forward:/WEB-INF/html/success.html";
		}
		return "forward:/WEB-INF/html/login.html";
	}
}
