package com.sie.attend.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
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
	/**
	 * 查询用户打卡方式
	 * 
	 */
	@RequestMapping(value = { "/queryLockMethod" }, produces = { "application/json" })
	@ResponseBody
	public Map<String, Object> queryLockMethod(@RequestParam Map<String,Object> params) {
		Map<String, Object> result = new HashMap<String,Object>();
		//测试
//		params.put("empName", "sdas");
//		params.put("offName", "交付科室");
//		params.put("depName", "资源共享部");
//		params.put("pageSize","5");
//		params.put("pageNumber","1");
	
		String empName = String.valueOf(params.get("empName"));
		String offName = String.valueOf(params.get("offName"));
		String depName = String.valueOf(params.get("depName"));
		int pageSize =Integer.valueOf(params.get("pageSize").toString());
		int pageNumber = Integer.valueOf(params.get("pageNumber").toString());
		
		int startShow = (pageNumber - 1) * pageSize;
		
		Map<String,Object> map = new HashMap<String,Object>(7);
		map.put("emp_name", empName);
		map.put("off_name", offName);
		map.put("dep_name", depName);
		map.put("startShow", startShow);
		map.put("pageSize", pageSize);
		List<Map<String, Object>> listParams = this.commonBO.selectList("com.sie.data.AdminMapper.selectByCondition", map);
		map = this.commonBO.selectOne("com.sie.data.AdminMapper.selectCount",map);
		
		result.put("total",map.get("count(*)".toString()));
		result.put("rows", listParams);
		
		return result;
	}
	
	/**
	 * 设置打卡方式（打卡机，系统）
	 */
	@RequestMapping(value = { "/setLockMethod" }, method = { RequestMethod.POST }, produces = { "application/json" })
	public Map<String, Object> setLockMethod(@RequestParam Map<String, Object> params) {
		Map<String, Object> result = new HashMap<String, Object>();
		Map<String, Object> data = new HashMap<String, Object>();
		//测试
//		params.put("empId", "123");
//		params.put("comeFrom", "machine");
		
		String emp_id = String.valueOf(params.get("empId"));
		String comefrom = String.valueOf(params.get("comeFrom"));
		
		if(!emp_id.equals("null")){
			Map<String,Object> map = new HashMap<String,Object>();
			map.put("emp_id",emp_id);
			map.put("comefrom",comefrom);
			this.commonBO.updateOne("com.sie.data.AdminMapper.setLockMethod", map);
			
			result.put("success", "yes");
			result.put("data", "修改成功");
		}else{
			result.put("success", "no");
			result.put("data", "修改失败");
		}
		return result;
	}
	/**
	 * 获取所有科室名称
	 */
	@RequestMapping(value = { "/getAllDept" })
	public List<Map<String, Object>> getAllDept(@RequestParam Map<String, Object> params) {
		List<Map<String, Object>> list=new ArrayList<Map<String,Object>>();
		list = this.commonBO.selectList("com.sie.data.AdminMapper.getAllDept");
		return list;
	}
	/**
	 * 获取所有部门名称
	 */
	@RequestMapping(value = { "/getAllOffice" })
	public List<Map<String, Object>> getAllOffice(@RequestParam Map<String, Object> params) {
		List<Map<String, Object>> list=new ArrayList<Map<String,Object>>();
		list = this.commonBO.selectList("com.sie.data.AdminMapper.getAllOffice");
		return list;
	}
}
