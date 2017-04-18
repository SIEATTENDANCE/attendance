package com.sie.attend.controller;


import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
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
@RequestMapping("/Deal")
public class DealController {
	@Resource(name = "commonBO")
	private CommonBO commonBO;

	
	//点击查询，查出所有要办理的异常订单
	//除了返回数据，还返回了每个订单的流水号，还返回该员工的角色
	@RequestMapping(value = { "/getDealException" }, method = { RequestMethod.GET}, produces = { "application/json" })
	public List<Map<String, Object>> getExceptionRecord(HttpServletRequest request) {
		int showPage = 1;// 默认第一页
		int pageSize = 5;// 默认显示5条记录
		
		String showStartTime = request.getParameter("showStartTime");//统一待办的开始接收时间
		String showEndTime = request.getParameter("showEndTime");//统一待办的结束接收时间
		String selectWho=request.getParameter("selectWho");//异常订单的发送人
		HttpSession session = request.getSession();
		String emp_id = (String) session.getAttribute("emp_id");
		showPage = Integer.parseInt(request.getParameter("showPage"));// 当第几页
		pageSize = Integer.parseInt(request.getParameter("pageSize"));//每一页多少数据
		int startShow = (showPage - 1) * pageSize;//分页计算
		
		List<Map<String, Object>> resultList=new ArrayList<>();
		Map<String, Object> resultMap = new HashMap<String, Object>();
		
		//判断用户的权限是那个
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("emp_id", emp_id);
		//查询用户的角色是那个
		Map<String, Object> empRole = this.commonBO.selectOne("com.sie.data.Deal.selectEmpRole",params);// 查询结果封装到list集合中
		if(empRole.get("name").equals("dep")){
			params.put("ex_node", "offic");//部长显示科室审核完成的
		}else if(empRole.get("name").equals("off")){
			params.put("ex_node", "new");//科室显示新提交的
		}else{
			//用户没有权限
			resultMap.put("result", "你当前没有审核的权限");
			resultList.add(resultMap);
			return resultList;
		}
		params.put("showStartTime", showStartTime);
		params.put("showEndTime", showEndTime);
		params.put("selectWho", selectWho);
		params.put("startShow", startShow);
		params.put("pageSize", pageSize);
		List<Map<String, Object>> DealException = this.commonBO.selectList("com.sie.data.Deal.selectException",params);// 查询结果封装到list集合中
		return DealException;
	}
	
	/*
	//点击审核按钮后触发
	@RequestMapping(value = { "/checkException" }, method = { RequestMethod.POST}, produces = { "application/json" })
	public Map<String, Object> editExceptionRecord(HttpServletRequest request) {	
		String exceptionNum = request.getParameter("exceptionNum");//异常申请单的流水号
		//测试数据
		//String exceptionNum="YCBG20170410002";
		
		Map<String, Object> resultMap = new HashMap<String, Object>();
		if("".equals(exceptionNum)||exceptionNum==null){
			resultMap.put("result", "请先点击一个要处理的异常的单号");
			return resultMap;
		}
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("exceptionNum", exceptionNum);
		//点击审核后,查询出异常订单以及订单下的异常打卡记录
		//先判断有没有附件
		Map<String, Object> ifAttach=this.commonBO.selectOne("com.sie.data.ExceptionRequest.selectifAttach",params);
		//System.out.println("判断情况");
		//System.out.println(ifAttach.get("count(*)").equals("0"));
		List<Map<String, Object>>  ExceptionAttach=null;
		if(!(ifAttach.get("count(*)").equals("0"))){
			//有附件
			  ExceptionAttach=this.commonBO.selectList("com.sie.data.ExceptionRequest.selectExceptionAttach",params);
		}
		Map<String, Object>  ExceptionRecord=this.commonBO.selectOne("com.sie.data.ExceptionRequest.selectExceptionNoAtt",params);
		List<Map<String, Object>> ExceptionRecordDetail=this.commonBO.selectList("com.sie.data.ExceptionRequest.selectExceptionRecordDetail",params);
		
		resultMap.put("ExceptionAttach", ExceptionAttach);
		resultMap.put("resultMap", ExceptionRecord);
		resultMap.put("resultList", ExceptionRecordDetail);
		return resultMap;
	}*/
	
	
	
	//点击审核通过按钮触发,还要传一个属于哪个角色roleName
		@RequestMapping(value = { "/passException" }, method = { RequestMethod.GET}, produces = { "application/json" })
		public Map<String, Object> commitExceptionChange(HttpServletRequest request) {	
			String ExceptionFloadId = request.getParameter("ExceptionFloadId");//异常申请单的流水号
			SimpleDateFormat dateStyle = new SimpleDateFormat("yyyy-MM-dd");
			String date = dateStyle.format(new Date());//需要审核通过的日期
			//String ExceptionFloadId="YCBG20170410003";//测试数据
			Map<String, Object> params = new HashMap<String, Object>();
			String reason = request.getParameter("reason");//部门或者科室的审批意见
			String roleName = request.getParameter("roleName");//判断属于那个角色
			
			params.put("ExceptionFloadId", ExceptionFloadId);
			params.put("date", date);
			params.put("roleName", roleName);
			params.put("reason", reason);
			
			Map<String, Object> resultMap = new HashMap<String, Object>();
			//更新审核状态字段
			int upResult=this.commonBO.updateOne("com.sie.data.Deal.updatetExceptionState",params);	
			if(upResult!=0){
			resultMap.put("result", "审核成功");
			}else {
				resultMap.put("result", "审核失败");
			}
			return resultMap;
		}
	
	
	
	
	
	//点击中止触发
	@RequestMapping(value = { "/stopExceptionChange" }, method = { RequestMethod.POST}, produces = { "application/json" })
	public Map<String, Object> stopExceptionChange(HttpServletRequest request) {	
		String ExceptionFloadId = request.getParameter("ExceptionFloadId");//异常申请单的id
		//String ExceptionFloadId="YCBG20170410003";//测试数据
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("ExceptionFloadId", ExceptionFloadId);
		params.put("ex_state", "stop");
		Map<String, Object> resultMap = new HashMap<String, Object>();
		//先判断有没有提交,提交了才能中止
		Map<String, Object> ifCommit=this.commonBO.selectOne("com.sie.data.ExceptionChange.ifCommit",params);
		if ((ifCommit.get("ex_state").toString().equals("commit"))) {
			resultMap.put("result", "异常订单还没有提交,不能中止");
			return resultMap;
		}else if (ifCommit.get("ex_state").toString().equals("stop")) {
			resultMap.put("result", "异常订单已经被中止了");	
			return resultMap;
		}else if (ifCommit.get("ex_state").toString().equals("over")) {
			resultMap.put("result", "异常订单完成了");	
			return resultMap;
		}else {
			if (!(ifCommit.get("ex_node").toString().equals("new"))) {
				resultMap.put("result", "异常订单在审核,不能中止了");
				return resultMap;
			}		
			
		}
		//更新异常表的ex_state字段
		int upResult=this.commonBO.updateOne("com.sie.data.ExceptionChange.updatetExceptionState",params);	
		if(upResult!=0){
		resultMap.put("result", "异常订单中止成功");
		}else {
			resultMap.put("result", "异常订单终止失败");
		}
		return resultMap;
	}
	
	
	

}
