package com.sie.attend.controller;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.sie.attend.common.bo.CommonBO;
import com.sie.attend.util.FloadNumber;

/**
 * 异常订单申请
 *
 */
@RestController // 等价于@controller加@ResponseBody
@RequestMapping("/ExceptionRequest")
public class ExceptionReqController {

	@Resource(name = "commonBO")
	private CommonBO commonBO;

	/**
	 * 查询用户的签到记录
	 * 
	 * @return
	 */
	//点击查询,异常申请触发
	@RequestMapping(value = { "/getExceptionRecord" }, method = { RequestMethod.POST}, produces = { "application/json" })
	public List<Map<String, Object>> getExceptionRecord(HttpServletRequest request) {
		String exceptionNum = request.getParameter("exceptionNum");//查询异常的编号
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
	
	//点击编辑按钮后触发
	@RequestMapping(value = { "/editExceptionRecord" }, method = { RequestMethod.GET}, produces = { "application/json" })
	public Map<String, Object> editExceptionRecord(HttpServletRequest request) {	
		String ExceptionFloadId = request.getParameter("ExceptionFloadId");//异常申请单的流水号
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("ExceptionFloadId", ExceptionFloadId);
		
		Map<String, Object> resultMap = new HashMap<String, Object>();
		//点击编辑后,查询出异常订单以及订单下的异常打卡记录
		Map<String, Object> ifCommit=this.commonBO.selectOne("com.sie.data.ExceptionChange.selectExceptionRecord",params);

		return resultMap;
	}
	
	
	//点击删除按钮后触发
	


}
