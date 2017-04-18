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
@RequestMapping("/ExceptionDetail")
public class ExceptionDetailController2 {

	@Resource(name = "commonBO")
	private CommonBO commonBO;
	/**
	 * 异常申请细节显示页面
	 * 
	 * @return
	 */
	//点击编辑按钮,审核按钮后触发
	//返回记录包括用户角色信息roleName;
	@RequestMapping(value = { "/editExceptionRecord" }, method = { RequestMethod.POST}, produces = { "application/json" })
	public Map<String, Object> editExceptionRecord(HttpServletRequest request) {	
		String exceptionNum = request.getParameter("exceptionNum");//异常申请单的流水号
		HttpSession session = request.getSession();
		String emp_id = (String) session.getAttribute("emp_id");
		//测试数据
		//String exceptionNum="YCBG20170410002";
		
		Map<String, Object> resultMap = new HashMap<String, Object>();
		if("".equals(exceptionNum)||exceptionNum==null){
			resultMap.put("result", "请先选择一个异常的单号");
			return resultMap;
		}
		
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("exceptionNum", exceptionNum);
		
		//查询用户的角色是那个
		params.put("emp_id", emp_id);
		Map<String, Object> empRole = this.commonBO.selectOne("com.sie.data.ExceptionDetail.selectEmpRole",params);
		if(empRole.get("name").equals("dep")){
			resultMap.put("roleName", "dep");
		}else if(empRole.get("name").equals("off")){
			resultMap.put("roleName", "off");
		}else{
			resultMap.put("roleName", "emp");
		}
		
		//点击编辑或审核后,查询出异常订单以及订单下的异常打卡记录
		//先判断有没有附件
		Map<String, Object> ifAttach=this.commonBO.selectOne("com.sie.data.ExceptionDetail.selectifAttach",params);
		//System.out.println("判断情况");
		//System.out.println(ifAttach.get("count(*)").equals("0"));
		List<Map<String, Object>>  ExceptionAttach=null;
		if(!(ifAttach.get("count(*)").equals("0"))){
			//有附件
			  ExceptionAttach=this.commonBO.selectList("com.sie.data.ExceptionDetail.selectExceptionAttach",params);
		}
		Map<String, Object>  ExceptionRecord=this.commonBO.selectOne("com.sie.data.ExceptionDetail.selectExceptionNoAtt",params);
		List<Map<String, Object>> ExceptionRecordDetail=this.commonBO.selectList("com.sie.data.ExceptionDetail.selectExceptionRecordDetail",params);
		
		resultMap.put("ExceptionAttach", ExceptionAttach);
		resultMap.put("ExceptionRecord", ExceptionRecord);
		resultMap.put("ExceptionRecordDetail", ExceptionRecordDetail);
		return resultMap;
	}
	


}
