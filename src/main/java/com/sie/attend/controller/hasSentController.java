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

/**
 * 待处理页面
 */
@RestController // 等价于@controller加@ResponseBody
@RequestMapping("/hasSent")
public class hasSentController {
	@Resource(name = "commonBO")
	private CommonBO commonBO;

	@RequestMapping(value = { "/gethasSent" }, method = { RequestMethod.GET }, produces = { "application/json" })
	public Map<String, Object> gethasSent(HttpServletRequest request) {
		int showPage = 1;// 默认第一页
		int pageSize = 5;// 默认显示5条记录

		HttpSession session = request.getSession();
		String emp_id = (String) session.getAttribute("emp_id");
		showPage = Integer.parseInt(request.getParameter("pageNumber"));// 当第几页
		pageSize = Integer.parseInt(request.getParameter("pageSize"));// 每一页多少数据
		int startShow = (showPage - 1) * pageSize;// 分页计算
		Map<String, Object> resultMap = new HashMap<String, Object>(5);
		
		

		Map<String, Object> params = new HashMap<String, Object>(10);
		params.put("emp_id", emp_id);
		params.put("startShow", startShow);
		params.put("pageSize", pageSize);
		
		//resultMap.put("total", selectCount.get("count(*)".toString()));//返回总记录的条数
		//resultMap.put("rows", DealException);//返回list数据
		
		return resultMap;
	}
}
