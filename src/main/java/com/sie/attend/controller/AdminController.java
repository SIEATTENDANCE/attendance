package com.sie.attend.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.sie.attend.common.bo.CommonBO;

@RestController
@RequestMapping(value = "/admin")
public class AdminController {
	@Resource
	private CommonBO commonBO;

	/**
	 * 显示打卡时间
	 */
	@RequestMapping(value = { "/showLockTime" }, method = { RequestMethod.GET}, produces = { "application/json" })
	@ResponseBody
	public Map<String, Object> showLockTime() {
		Map<String, Object> result = new HashMap<String, Object>();
		Map<String, Object> data = new HashMap<String, Object>();

		List<Map<String, Object>> realTimeList = this.commonBO.selectList("com.sie.data.AdminMapper.showLockTime");
		data.put("dataList", realTimeList);
		result.put("success", "yes");
		result.put("data", data);
		return result;
	}

	/**
	 * 设置打卡时间
	 */
	@RequestMapping(value = { "/setLockTime" }, method = { RequestMethod.GET}, produces = { "application/json" })
	public Map<String, Object> setLockTime(@RequestParam Map<String,Object> params) {
		Map<String, Object> result = new HashMap<String, Object>();
		Map<String, Object> data = new HashMap<String, Object>();
		
		//测试
//		params.put("time1", "5:00");
//		params.put("time2", "7:00");
//		params.put("time3", "13:00");
//		params.put("time4", "18:00");
		
		String time1 = String.valueOf(params.get("time1"));
		String time2 = String.valueOf(params.get("time2"));
		String time3 = String.valueOf(params.get("time3"));
		String time4 = String.valueOf(params.get("time4"));
		
		Map<String,Object> times = new HashMap<String,Object>();
		times.put("time1", time1);
		times.put("time2", time2);
		times.put("time3", time3);
		times.put("time4", time4);
		
		this.commonBO.updateOne("com.sie.data.AdminMapper.setLockTime", times);
		result.put("success", "yes");
		result.put("data", "修改成功");
		return result;
	}

}
