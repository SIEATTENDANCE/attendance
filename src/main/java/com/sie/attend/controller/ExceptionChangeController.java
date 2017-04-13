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
 * 签到签退记录
 *
 */
@RestController // 等价于@controller加@ResponseBody
@RequestMapping("/ExceptionChange")
public class ExceptionChangeController {

	@Resource(name = "commonBO")
	private CommonBO commonBO;

	/**
	 * 点击新增后进入后的页面 
	 * @return
	 */

	//添加异常变更申请,点击保存触发
	@RequestMapping(value = { "/addExceptionChange" }, method = { RequestMethod.GET}, produces = { "application/json" })
	public Map<String, Object> addExceptionChange(HttpServletRequest request) {
		SimpleDateFormat dateStyle = new SimpleDateFormat("yyyy-MM-dd");
		String date = dateStyle.format(new Date());
		
		String excepchange=request.getParameter("excepchange");//获取到的是要申请的异常串,操作明细表
		String exc_cha_id = request.getParameter("exc_cha_id");//获取附件的地址,操作附件表
		
		String note = request.getParameter("note");//获取备注信息
		HttpSession session = request.getSession();// 获取用户信息
		String username = (String) session.getAttribute("emp_id");
		String foladNumber=FloadNumber.getFloadNumber(date);//得到一个流水号
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("username", username);
		params.put("fload_id", foladNumber);
		params.put("date", date);
		params.put("note", note);
		//插入异常变更单
		this.commonBO.insertOne("com.sie.data.ExceptionChange.insertExceptionChange",params);
		//插入异常明细表
	/*	String[] id_reasons = excepchange.split("-");
		int sqlResult = 0;
		for (int i = 0; i < id_reasons.length; i++) {
	            //System.out.println(id_reasons[i]);
	            id_reason = id_reasons[i].split("[*]");
//				System.out.println(id_reason.length);
//	            for(int j = 0; j < id_reason.length; j++) {
//	            	System.out.println(id_reason[j]);
	            
//	            }
	            int id = 0;
	            String reason = "";
	            //判断有没有原因，有原因length为2，没有只有id那么就1
	            if(id_reason.length == 1) {
	            	id = Integer.parseInt(id_reason[0]);
	            } else if(id_reason.length == 2) {
	            	id = Integer.parseInt(id_reason[0]);
	            	reason = id_reason[1];
	            }
		}
		
		Map<String, Object> params2 = new HashMap<String, Object>();
		params2.put("id", id);//异常记录的id
		params2.put("reason", reason);//异常记录原因
		params2.put("fload_id", foladNumber);//流水号
*/			
		//插入附件表	
		
		
		
		
		//保存后返回一个异常的id,(用户保存后点击提交会用到)
		
		
		
		Map<String, Object> resultMap = new HashMap<String, Object>();
		resultMap.put("result", "异常申请单提交成功");
		return resultMap;
	}
	
	//点击提交按钮触发
	@RequestMapping(value = { "/commitExceptionChange" }, method = { RequestMethod.GET}, produces = { "application/json" })
	public Map<String, Object> commitExceptionChange(HttpServletRequest request) {	
		HttpSession session = request.getSession();// 获取用户信息
		String username = (String) session.getAttribute("emp_id");
		String ExceptionChangeId = request.getParameter("ExceptionChangeId");//异常申请单的id
		
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("username", username);
		params.put("ExceptionChangeId", ExceptionChangeId);
		
		Map<String, Object> resultMap = new HashMap<String, Object>();
		//先判断有没有保存,保存了才能提交
		Map<String, Object> ifCommit=this.commonBO.selectOne("com.sie.data.ExceptionChange.ifCommit",params);

		if (ifCommit.get("ex_state").toString().equals("save")) {
			resultMap.put("result", "请先保存订单");
			return resultMap;
		}	
		//更新异常表的ex_state字段
		int upResult=this.commonBO.updateOne("com.sie.data.ExceptionState.updatetExceptionRecord",params);	
		

		if(upResult!=0){
		resultMap.put("result", "提交成功");
		}else {
			resultMap.put("result", "提交失败");
		}
		
		return resultMap;
	}
	
	
	
	
	//点击中止触发
	@RequestMapping(value = { "/stopExceptionChange" }, method = { RequestMethod.GET}, produces = { "application/json" })
	public Map<String, Object> stopExceptionChange(HttpServletRequest request) {	
		HttpSession session = request.getSession();// 获取用户信息
		String username = (String) session.getAttribute("emp_id");
		//先判断有没有提交，提交了才能中止异常
		
		
		
		
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("username", username);
	
		int upResult=this.commonBO.updateOne("com.sie.data.ExceptionChange.selectExceptionRecord",params);	
		Map<String, Object> resultMap = new HashMap<String, Object>();

		if(upResult!=0){
		resultMap.put("result", "中止成功");
		}else {
			resultMap.put("result", "中止失败");
		}
		
		return resultMap;
	}
	
	
	
	
	
	
	
	//查询异常记录,点击+号按钮触发弹出所有异常记录
	@RequestMapping(value = { "/selectExceptionRecord" }, method = { RequestMethod.GET}, produces = { "application/json" })
	public List<Map<String, Object>> selectExceptionRecord(HttpServletRequest request) {	
		int showPage = 1;// 默认第一页
		int pageSize = 5;// 默认显示5条记录
		
		HttpSession session = request.getSession();// 获取用户信息
		String username = (String) session.getAttribute("emp_id");
		showPage = Integer.parseInt(request.getParameter("showPage"));// 当第几页
		pageSize = Integer.parseInt(request.getParameter("pageSize"));//每一页多少数据
		
		
		int startShow = (showPage - 1) * pageSize;//分页计算
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("username", username);
		params.put("startShow", startShow);
		params.put("pageSize", pageSize);
		List<Map<String, Object>> exceptionRecord = this.commonBO.selectList("com.sie.data.ExceptionChange.selectExceptionRecord",params);	
		return exceptionRecord;
	}
	
	//添加异常的打卡记录后的页面点击确定触发
	@RequestMapping(value = { "/selectChoiceExcep" }, method = { RequestMethod.GET}, produces = { "application/json" })
	public List<Map<String, Object>> selectChoiceExcep(HttpServletRequest request) {		
		String choiceRecordId="15-16-18";
		choiceRecordId = request.getParameter("choiceRecordId");//获取备注信息
		String[] choices=choiceRecordId.split("-");//前端传递数据的方式：id-id-id
		
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>> ();
		Map<String, Object> params = new HashMap<String, Object>();
		
		for (int i = 0; i < choices.length; i++) {
			params.put("choice", choices[i]);
			Map<String, Object> exceptionRecord = this.commonBO.selectOne("com.sie.data.ExceptionChange.selectChoiceExcep",params);	
		   list.add(exceptionRecord);
		}
		return list;
	}
	
	


}
