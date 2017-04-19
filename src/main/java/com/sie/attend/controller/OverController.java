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

	
	@RequestMapping(value = { "/getOverException" }, method = { RequestMethod.GET }, produces = { "application/json" })
	public Map<String, Object> getOverException(HttpServletRequest request) {
		int showPage = 1;// 默认第一页
		int pageSize = 5;// 默认显示5条记录
		String showStartTime = request.getParameter("showStartTime");// 统一已办的开始接收时间
		String showEndTime = request.getParameter("showEndTime");// 统一已办的结束接收时间
		String selectWho = request.getParameter("selectWho");// 异常订单的发送人
		HttpSession session = request.getSession();
		String emp_id = (String) session.getAttribute("emp_id");
		showPage = Integer.parseInt(request.getParameter("pageNumber"));// 当第几页
		pageSize = Integer.parseInt(request.getParameter("pageSize"));// 每一页多少数据
		int startShow = (showPage - 1) * pageSize;// 分页计算

		Map<String, Object> resultMap = new HashMap<String, Object>(5);

		// 判断用户的权限是那个
		Map<String, Object> params = new HashMap<String, Object>(10);
		params.put("emp_id", emp_id);
		// 查询用户的角色是那个
		Map<String, Object> empRole = this.commonBO.selectOne("com.sie.data.Over.selectEmpRole", params);// 查询结果封装到list集合中
		if (empRole.get("name").equals("dep")) {
			params.put("ex_node", "depover");// 部长显示部长审核完成的
		} else if (empRole.get("name").equals("off")) {
			params.put("ex_node", "offover");// 科室显示科室审核完成的
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
		List<Map<String, Object>> DealException = this.commonBO.selectList("com.sie.data.Over.selectException", params);// 查询结果封装到list集合中
		//查询总记录数
		Map<String, Object> selectCount=this.commonBO.selectOne("com.sie.data.Over.selectExceptionCount",params);
		resultMap.put("total", selectCount.get("count(*)".toString()));//返回总记录的条数
		resultMap.put("rows", DealException);//返回list数据
		return resultMap;
	}
	
	


}
