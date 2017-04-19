package com.sie.attend.controller;

import java.util.HashMap;
import java.util.Map;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import com.sie.attend.common.bo.CommonBO;
/**
 * 管理员模块
 * @author
 *
 */
@RestController
@RequestMapping(value = "/admin")
public class AdminController {
	@Resource
	private CommonBO commonBO;

	/**
	 * 显示打卡时间
	 */
	@RequestMapping(value = { "/showLockTime" }, method = { RequestMethod.POST}, produces = { "application/json" })
	public Map<String, Object> showLockTime() {
		Map<String, Object> resultMap = this.commonBO.selectOne("com.sie.data.AdminMapper.showLockTime");
		return resultMap;
	}

	/**
	 * 设置打卡时间
	 */
	@RequestMapping(value = { "/setLockTime" }, method = { RequestMethod.POST}, produces = { "application/json" })
	public Map<String, Object> setLockTime(HttpServletRequest request) {
		Map<String, Object> resultMap = new HashMap<String, Object>(1);
		
		String time1 = request.getParameter("time1");
		String time2 = request.getParameter("time2");
		String time3 = request.getParameter("time3");
		String time4 = request.getParameter("time4");
		
		Map<String,Object> map = new HashMap<String,Object>(4);
		map.put("time1", time1);
		map.put("time2", time2);
		map.put("time3", time3);
		map.put("time4", time4);
		
		int result = this.commonBO.updateOne("com.sie.data.AdminMapper.setLockTime", map);
		resultMap.put("result", result);
		return resultMap;
	}

}
