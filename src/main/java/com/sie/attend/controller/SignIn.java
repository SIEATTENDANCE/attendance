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
/**
 * 签到签退记录
 *
 */
@RestController  //等价于@controller加@ResponseBody
@RequestMapping("/signIn")
public class SignIn {

	@Resource(name="commonBO")
	private CommonBO commonBO;
	
	/**
	 * 查询用户的签到记录
	 * @return
	 */
	@RequestMapping(value = { "/getUserSignRecord" }, method = {
			RequestMethod.POST }, produces = { "application/json" })
	public Map<String, Object> getUserSignRecord(HttpServletRequest request) {

		SimpleDateFormat dateStyle = new SimpleDateFormat("yyyy-MM-dd");
		String date = dateStyle.format(new Date());	
		
		String datestart=date;//查询打卡情况的起始时间
		String dateend =date;//查询打卡情况的结束时间
		String attendstate="all";//查询打卡记录的状态
	
		datestart = request.getParameter("startDay");
		attendstate = request.getParameter("signState");
		request.getParameter("endDay");
		
		//获取用户信息
		HttpSession session = request.getSession();
		String username = (String)session.getAttribute("emp_id");
		
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("username", username);
		params.put("datestart", datestart);
		params.put("dateend", dateend);
		params.put("attendstate", attendstate);
		
		List<Map<String, Object>> list = new ArrayList<>();
		list = this.commonBO.selectSignRecord("com.sie.data.Sign.SelectAllSignByUser", params);//查询结果封装到list集合中
		
		//将获取到的list集合里面的数据分页显示
		
	    System.out.println(list);	

		int pageIndex = Integer.parseInt(request.getParameter("pageIndex"));//第几页，从0开始
		int pageSize =  Integer.parseInt(request.getParameter("pageSize"));//每一页多少数据	
	

        
        Map<String, Object> mapList = new HashMap<>();
      
        
		return mapList;
	}

	
}
