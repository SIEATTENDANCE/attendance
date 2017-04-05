package com.sie.attend.controller;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.sie.attend.common.bo.CommonBO;

@RestController
@RequestMapping("/login")
public class LoginController {

	@Resource
	private CommonBO commonBO;
	/**
	 * 验证登录信息
	 * @param request
	 * @return
	 */
	@RequestMapping(value = { "/verify" }, method = 
			RequestMethod.POST, produces = { "application/json" })
	public Map<String, Object> verify(HttpServletRequest request) {
		Map<String, Object> returnMap = new HashMap<String, Object>();
		// 获取页面传回的用户名和密码
		String emp_id = request.getParameter("emp_id");
		String user_pwd = request.getParameter("user_pwd");
		//判断输入框是否为空
		if ("".equals(emp_id) || emp_id == null || "".equals(user_pwd) || user_pwd == null) {
			returnMap.put("returnMess", -2);
			return returnMap;//账号或密码没输入
		}
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("emp_id", emp_id);
		map = commonBO.selectOne("com.sie.attend.pojo.LoginMapper.selectByEmpId", map);
		if (map == null || map.size() == 0) {
			returnMap.put("returnMess", 0);
			return returnMap;//查无此账号
		}
		if (!(map.get("emp_pwd").equals(user_pwd))) {
			returnMap.put("returnMess", -1);
			return returnMap;//密码错误
		}
		HttpSession session = request.getSession();
		System.out.println(map.get("emp_id"));
		session.setAttribute("user_name", map.get("emp_id"));
		returnMap.put("returnMess", 1);
		return returnMap;//校验成功
	}
}
