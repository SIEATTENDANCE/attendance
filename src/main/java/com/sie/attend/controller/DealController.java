package com.sie.attend.controller;

import java.text.SimpleDateFormat;
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

	// 点击查询，查出所有要办理的异常订单
	// 除了返回数据，还返回了每个订单的流水号，还返回该员工的角色
	@RequestMapping(value = { "/getDealException" }, method = { RequestMethod.POST }, produces = { "application/json" })
	public Map<String, Object> getDealException(HttpServletRequest request) {
		int showPage = 1;// 默认第一页
		int pageSize = 5;// 默认显示5条记录

		String showStartTime = request.getParameter("showStartTime");// 统一待办的开始接收时间
		String showEndTime = request.getParameter("showEndTime");// 统一待办的结束接收时间
		String selectWho = request.getParameter("selectWho");// 异常订单的发送人
		HttpSession session = request.getSession();
		String emp_id = (String) session.getAttribute("emp_id");
		showPage = Integer.parseInt(request.getParameter("pageNumber"));// 当第几页
		pageSize = Integer.parseInt(request.getParameter("pageSize"));// 每一页多少数据
	 		
		int startShow = (showPage - 1) * pageSize;// 分页计算
		
		//测试数据
	/*	String showStartTime="2017-04-01";
		String showEndTime="2017-04-20";
		String emp_id="222";
		String selectWho=null;*/
		
		Map<String, Object> resultMap = new HashMap<String, Object>(5);

		// 判断用户的权限是那个
		Map<String, Object> params = new HashMap<String, Object>(10);
		params.put("emp_id", emp_id);
		// 查询用户的角色是那个
		Map<String, Object> empRole = this.commonBO.selectOne("com.sie.data.Deal.selectEmpRole", params);// 查询结果封装到list集合中
		if (empRole.get("name").equals("dep")) {
			params.put("ex_node", "offover");// 部长显示科室审核完成的
		} else if (empRole.get("name").equals("off")) {
			params.put("ex_node", "new");// 科室显示新提交的
		} else {
			// 用户没有权限
			resultMap.put("result", "你当前没有审核的权限");
			return resultMap;
		}
		params.put("showStartTime", showStartTime);
		params.put("showEndTime", showEndTime);
		params.put("selectWho", selectWho);
		params.put("startShow", startShow);
		params.put("pageSize", pageSize);
		List<Map<String, Object>> DealException = this.commonBO.selectList("com.sie.data.Deal.selectException", params);// 查询结果封装到list集合中
		//查询总记录数
		Map<String, Object> selectCount=this.commonBO.selectOne("com.sie.data.Deal.selectExceptionCount",params);
		resultMap.put("total", selectCount.get("count(*)".toString()));//返回总记录的条数
		resultMap.put("rows", DealException);//返回list数据
		return resultMap;
	}

	// 点击审核通过按钮触发,还要传一个属于哪个角色roleName
	@RequestMapping(value = { "/passException" }, method = { RequestMethod.POST }, produces = { "application/json" })
	public Map<String, Object> passException(HttpServletRequest request) {
		String ExceptionFloadId = request.getParameter("ExceptionFloadId");// 异常申请单的流水号
		String reason = request.getParameter("opinion");// 部门或者科室的审批意见
		String roleName = request.getParameter("roleName");// 判断属于那个角色
		SimpleDateFormat dateStyle = new SimpleDateFormat("yyyy-MM-dd");
		String date = dateStyle.format(new Date());// 需要审核通过的日期
	/*	//测试数据
		String ExceptionFloadId="YCBG20170410002";
		String reason="测试_没意见";
		String roleName="dep";
		*/
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("ExceptionFloadId", ExceptionFloadId);
		params.put("date", date);
		params.put("roleName", roleName);
		params.put("reason", reason);

		Map<String, Object> resultMap = new HashMap<String, Object>();
		// 更新审核状态字段
		int upResult=0;
		if("dep".equals(roleName)){
		 upResult = this.commonBO.updateOne("com.sie.data.Deal.updatetExceptionDepState", params);
		}else{
		 upResult = this.commonBO.updateOne("com.sie.data.Deal.updatetExceptionOffState", params);
		}
		// 如果是部门审核通过更新异常的数据
		if ("dep".equals(roleName)) {
			// 通过流水号,查询异常的记录，更新异常打卡记录变为正常
			List<Map<String, Object>> listID = this.commonBO.selectList("com.sie.data.Deal.selectListId", params);
			for (Map<String, Object> map : listID) {
				params.put("recd_id", map.get("recd_id"));
				 this.commonBO.updateOne("com.sie.data.Deal.updatetExceptionRecord", params);
			}
		}
		if (upResult != 0) {
			resultMap.put("result", "审核成功");
		} else {
			resultMap.put("result", "审核失败");
		}
		return resultMap;
	}

	// 科室或部长负责人点击中止触发
	@RequestMapping(value = { "/stopExceptionChange" }, method = { RequestMethod.POST }, produces = {
			"application/json" })
	public Map<String, Object> stopExceptionChange(HttpServletRequest request) {
		String ExceptionFloadId = request.getParameter("ExceptionFloadId");// 异常申请单的id
		// String ExceptionFloadId="YCBG20170410003";//测试数据
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("ExceptionFloadId", ExceptionFloadId);
		params.put("ex_state", "stop");
		Map<String, Object> resultMap = new HashMap<String, Object>();
		Map<String, Object> ifCommit = this.commonBO.selectOne("com.sie.data.ExceptionChange.ifCommit", params);
		if (ifCommit.get("ex_state").equals("stop")) {
			resultMap.put("result", "异常订单已经被中止了");
			return resultMap;
		}
		// 更新异常表的ex_state字段
		int upResult = this.commonBO.updateOne("com.sie.data.Deal.updatetExceptionState", params);
		if (upResult != 0) {
			resultMap.put("result", "异常订单中止成功");
		} else {
			resultMap.put("result", "异常订单终止失败");
		}
		return resultMap;
	}

}
