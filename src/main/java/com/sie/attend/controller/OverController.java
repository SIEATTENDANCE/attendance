package com.sie.attend.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import com.sie.attend.common.bo.CommonBO;

/**
 * 已办页面
 *
 */
@RestController // 等价于@controller加@ResponseBody
@RequestMapping("/Over")
public class OverController {
	@Resource(name = "commonBO")
	private CommonBO commonBO;
	
	
	
	
	//点击查询,异常申请触发
	@RequestMapping(value = { "/getExceptionRecord" }, method = { RequestMethod.POST}, produces = { "application/json" })
	public List<Map<String, Object>> getExceptionRecord(HttpServletRequest request) {
		String exceptionNum = request.getParameter("exceptionNum");//查询异常的流水号
		String showStartTime = request.getParameter("showStartTime");//查询异常申请的起始时间
		String showEndTime = request.getParameter("showEndTime");//查询异常申请的结束时间
		String exceptionState = request.getParameter("exceptionState");//要查询的异常状态
		HttpSession session = request.getSession();// 获取用户信息
		String username = (String) session.getAttribute("emp_id");	
		//测试数据
/*		String exceptionNum = null;
		String showStartTime = "2017-04-07";
		String showEndTime = "2017-04-18";
		String exceptionState = "new";
		String username = "123";*/
		
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("username", username);
		params.put("exceptionNum", exceptionNum);
		params.put("showStartTime", showStartTime);
		params.put("showEndTime", showEndTime);
		params.put("exceptionState", exceptionState);
		List<Map<String, Object>> exceptionRecord = this.commonBO.selectList("com.sie.data.ExceptionRequest.selectException",params);// 查询结果封装到list集合中
		System.out.println("exceptionRecord中的Map"+exceptionRecord);
		return exceptionRecord;
	}
	
	
	


}
