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
 * 待处理页面
 */
@RestController // 等价于@controller加@ResponseBody
@RequestMapping("/HasSent")
public class HasSentController {
	@Resource(name = "commonBO")
	private CommonBO commonBO;
	//用户查看所有已经发送的异常,返回异常记录的流水号,和当前处理节点
	@RequestMapping(value = { "/gethasSent" }, method = { RequestMethod.POST }, produces = { "application/json" })
	public Map<String, Object> gethasSent(HttpServletRequest request) {
		int pageNumber = 1;// 默认第一页
		int pageSize = 5;// 默认显示5条记录
		HttpSession session = request.getSession();
		String emp_id = (String) session.getAttribute("emp_id");
		pageNumber = Integer.parseInt(request.getParameter("pageNumber"));// 当第几页
		pageSize = Integer.parseInt(request.getParameter("pageSize"));// 每一页多少数据
		//测试数据
		//String emp_id="123";
		int startShow = (pageNumber - 1) * pageSize;// 分页计算
		Map<String, Object> resultMap = new HashMap<String, Object>(5);
		Map<String, Object> params = new HashMap<String, Object>(5);
		params.put("emp_id", emp_id);
		params.put("startShow", startShow);
		params.put("pageSize", pageSize);
		//查看当前用户发送的数据
		List<Map<String,Object>> hasSentRecord=this.commonBO.selectList("com.sie.data.HasSent.selectHasSentRecord",params);
		Map<String,Object> selectCount=this.commonBO.selectOne("com.sie.data.HasSent.selectHasSentCount",params);
		
		resultMap.put("total", selectCount.get("count(*)".toString()));//返回总记录的条数
		resultMap.put("rows", hasSentRecord);//返回list数据
		return resultMap;
	}
	//点击查看发送状态触发,需要传递一个异常的流水号
	@RequestMapping(value = { "/recallSent" }, method = { RequestMethod.POST }, produces = { "application/json" })
	public Map<String, Object>  recallSent(HttpServletRequest request){
		String ExceptionFloadId=request.getParameter("ExceptionFloadId");
		HttpSession session = request.getSession();
		String emp_id = (String) session.getAttribute("emp_id");
		//测试数据
		//String ExceptionFloadId="YCBG201704181727";
		Map<String,Object> params=new HashMap<String,Object>();
		params.put("ExceptionFloadId", ExceptionFloadId);
		params.put("emp_id", emp_id);
		Map<String, Object> resultMap=this.commonBO.selectOne("com.sie.data.HasSent.selectSentState",params);
		return resultMap;
	}
	//点击撤回异常订单,需要传递一个异常的流水号
	@RequestMapping(value = { "/recall" }, method = { RequestMethod.POST }, produces = { "application/json" })
	public Map<String, Object>  recall(HttpServletRequest request){
		String ExceptionFloadId=request.getParameter("ExceptionFloadId");
		HttpSession session = request.getSession();
		String emp_id = (String) session.getAttribute("emp_id");
		//测试数据
		//String ExceptionFloadId="YCBG201704181727";
		Map<String,Object> params=new HashMap<String,Object>();
		params.put("ExceptionFloadId", ExceptionFloadId);
		params.put("ex_state", "stop");
		params.put("emp_id", emp_id);
		int result=this.commonBO.updateOne("com.sie.data.HasSent.updatetExceptionState",params);
		Map<String, Object> resultMap=new HashMap<String,Object>(3);
		if(result!=0){
			resultMap.put("result", "撤回成功");
		}else {
			resultMap.put("result", "撤回失败");
		}
		return resultMap;
	}
}
