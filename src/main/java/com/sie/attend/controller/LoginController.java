package com.sie.attend.controller;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.sie.attend.common.bo.CommonBO;

@RestController
public class LoginController {
	
	@Resource
	private CommonBO commonBO;

	@RequestMapping(value="login",method=RequestMethod.POST)
	public int verify(HttpServletRequest request){
		String name = request.getParameter("name");
		String pwd = request.getParameter("pwd");
		if("".equals(name) || name == null || "".equals(pwd) || pwd ==null){
			return 0;
		}
		if("test".equals(name) && "test".equals(pwd)){
			return 1;
		}
		return 2;
	}
}
