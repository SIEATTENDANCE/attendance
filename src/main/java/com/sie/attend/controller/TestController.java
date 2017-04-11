package com.sie.attend.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.sie.attend.common.bo.CommonBO;

@RestController
@RequestMapping("/test")
public class TestController {
	@Resource
	private CommonBO commonBO;
	
	/*
	@RequestMapping(value = { "/find/{id}" }, method = {
			org.springframework.web.bind.annotation.RequestMethod.GET }, produces = { "application/json" })
	public Map<String,Object> find(@PathVariable("id") String id) {
		System.out.println(id);
		Map<String, Object> m = new HashMap<String, Object>();
		m.put("id",  id);
		return this.commonBO.selectUser("com.sie.attend.pojo.userMapper.selectUser", m);
	}
	*/
	
	@RequestMapping("tologin")
	public String test(){
		
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("username", "123");
		params.put("datestart", "2017-04-05");
		params.put("dateend", "2017-04-08");
		params.put("attendstate", "normal");
		List<Map<String, String>> list = commonBO.selectSignRecord("com.sie.data.Sign.SelectAllSignByUser", params);//查询结果封装到list集合中
		System.out.println(list);
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
