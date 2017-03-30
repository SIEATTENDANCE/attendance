package com.sie.attend.controller;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.sie.attend.common.bo.CommonBO;

@RestController
@RequestMapping("/test")
public class TestController {
	@Resource
	private CommonBO commonBo;
	
	@RequestMapping(value = { "/find/{id}" }, method = {
			org.springframework.web.bind.annotation.RequestMethod.GET }, produces = { "application/json" })
	public Map<String,Object> find(@PathVariable("id") String id) {
		System.out.println(id);
		Map<String, Object> m = new HashMap<String, Object>();
		m.put("id",  id);
		System.out.println(this.commonBo.selectUser("com.sie.attend.pojo.userMapper.selectUser", m));
		return this.commonBo.selectUser("com.sie.attend.pojo.userMapper.selectUser", m);
	}
	
	@RequestMapping("/map/{id}")
	public Map<String,Object> test(@PathVariable Integer id){
		System.out.println(id);
		Map<String,Object> m = new HashMap<String,Object>();
		m.put("id", id);
		return this.commonBo.selectUser("com.sie.attend.pojo.userMapper.selectUser", m);
	}
	
	/*
	 * 使用restController后无法使用视图解析器，所以页面无法跳转，直接显示字符串
	 */
	@RequestMapping("tologin")
	public String test(){
		System.out.println("测试页面跳转");
		return "login";
	}
	
	//测试页面跳转
	@RequestMapping("toTest")
	public ModelAndView test1(){
		ModelAndView mv = new ModelAndView("login");
		System.out.println("登录页面");
		return mv;
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
