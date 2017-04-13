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
@RestController // 等价于@controller加@ResponseBody
@RequestMapping("/signIn")
public class SignInController {

	@Resource(name = "commonBO")
	private CommonBO commonBO;

	/**
	 * 查询用户的签到记录
	 * 
	 * @return
	 */
	@RequestMapping(value = { "/getSignRecord" }, method = { RequestMethod.POST }, produces = { "application/json" })
	public Map<String, Object> getSignRecord(HttpServletRequest request) {
		
		Map<String,Object> resultMap = new HashMap<String,Object>(2);
		
//		SimpleDateFormat dateStyle = new SimpleDateFormat("yyyy-MM-dd");
//		String date = dateStyle.format(new Date());
//		String datestart = date;// 查询打卡情况的起始时间
//		String dateend = date;// 查询打卡情况的结束时间
//		String attendstate = "all";// 查询打卡记录的状态
//		int showPage = 1;// 默认第一页
//		int pageSize = 10;// 默认显示5条记录

		String datestart = request.getParameter("startDay");// 查询打卡记录的起始时间
		String dateend = request.getParameter("endDay");// 查询打卡记录的截至时间
		String attendstate = request.getParameter("signState");// 查询要打卡记录的状态
		HttpSession session = request.getSession();// 获取用户信息
		String username = (String) session.getAttribute("emp_id");
		int pageSize =Integer.parseInt(request.getParameter("pageSize"));
		int pageNumber = Integer.parseInt(request.getParameter("pageNumber"));

		/*
		 * //测试数据 datestart="2017-04-05"; String username ="123";
		 */
/*
		System.out.println("signin方法中的值" + datestart);
		System.out.println("signin方法中的值" + attendstate);
		System.out.println("signin方法中的值" + dateend);
		System.out.println("signin方法中的值" + username);

	*/	
		int startShow = (pageNumber - 1) * pageSize;
		// 分页(判断查询从第几条数据开始查，然后查多少条)
		Map<String, Object> params = new HashMap<String, Object>(7);
		params.put("username", username);
		params.put("datestart", datestart);
		params.put("dateend", dateend);
		params.put("attendstate", attendstate);
		params.put("startShow", startShow);
		params.put("pageSize", pageSize);
		
		List<Map<String, Object>> list = this.commonBO.selectList("com.sie.data.Sign.SelectAllSignByUser", params);// 查询结果封装到list集合中
		params = this.commonBO.selectOne("com.sie.data.Sign.selectSignCount",params);

		// 将获取到的list集合里面的数据分页显示
//		System.out.println("signin中分页显示数据" + list);
		
		resultMap.put("total", params.get("count(*)".toString()));
		resultMap.put("rows", list);
		
		return resultMap;
	}

	// 签到业务
	@RequestMapping(value = { "/UserSignIn" }, method = { RequestMethod.POST }, produces = { "application/json" })
	public Map<String, Object> UserSignIn(HttpServletRequest request) {

		HttpSession session = request.getSession();
		String username = (String) session.getAttribute("emp_id");

		Map<String, Object> resultMap = new HashMap<String, Object>(3); // 返回签到的结果
		// 获取当天日期和时间，分开
		SimpleDateFormat dateStyle = new SimpleDateFormat("yyyy-MM-dd");
		SimpleDateFormat TimeStyle = new SimpleDateFormat("HH:mm:ss");
		String date = dateStyle.format(new Date());
		String time = TimeStyle.format(new Date());

		// 测试数据
/*        		
        username = "123";
		date = "2017-03-12";
		time = "13:30:28";
		*/

		// 判断是否已经签到
		Map<String, Object> ifSign = new HashMap<String, Object>(10);
		ifSign.put("username", username);
		ifSign.put("date", date);
		ifSign.put("recd_inout", "in");
		ifSign = this.commonBO.selectOne("com.sie.data.Sign.SelectUserIfSign", ifSign);
		//System.out.println("签到记录"+ifSign);
		if (!(ifSign.get("count(*)").toString().equals("0"))) {
			resultMap.put("result", "你今天已经签到过了");
			return resultMap;
		}
		// 查询签到的时间,判断是否迟到
		Map<String, Object> cheskSigntime = this.commonBO.selectOne("com.sie.data.Sign.SelectCheskSigntime");
		String signState = "except";
		if (time.compareTo(cheskSigntime.get("time1").toString()) < 0
				|| time.compareTo(cheskSigntime.get("time1").toString()) == 0) {
			signState = "normal";
			resultMap.put("result", "签到成功，正常出勤");
		} else {
			if (time.compareTo(cheskSigntime.get("time2").toString()) > 0
					|| time.compareTo(cheskSigntime.get("time2").toString()) == 0) {
				signState = "nowork";
				resultMap.put("result", "签到成功，状态旷工！");
			} else {
				signState = "late";
				resultMap.put("result", "签到成功，你迟到了！");
			}
		}
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("username", username);
		params.put("date", date);
		params.put("time", time);
		params.put("real_time", cheskSigntime.get("time1"));
		params.put("work_state", signState);
		params.put("mor_noon", "mor");
		params.put("comefrom", cheskSigntime.get("rec_type"));
		int insertSignMor = this.commonBO.insertOne("com.sie.data.Sign.InsertSignMorRecord", params);
		//System.out.println("插入上午的签到数据成功" + insertSignMor);
		params.put("mor_noon", "noon");
		int insertSignNoon = this.commonBO.insertOne("com.sie.data.Sign.InsertSignNoonRecord", params);
		//System.out.println("插入下午签退异常数据成功" + insertSignNoon);
		return resultMap;

	}

	// 签退业务
	/**
	 * 签退
	 */
	@RequestMapping(value = { "/UserSignOut" }, method = { RequestMethod.POST }, produces = { "application/json" })
	public Map<String, Object> UserSignOut(HttpServletRequest request) {
		HttpSession session = request.getSession();
		String username = (String) session.getAttribute("emp_id");
		Map<String, Object> resultMap = new HashMap<String, Object>();

		// 获取当天日期和时间，分开
		SimpleDateFormat dateStyle = new SimpleDateFormat("yyyy-MM-dd");
		SimpleDateFormat TimeStyle = new SimpleDateFormat("HH:mm:ss");
		String date = dateStyle.format(new Date());
		String time = TimeStyle.format(new Date());
		//签退测试数据
/*	     		
        username ="123";
		date = "2017-03-12";
		time = "15:30:28";
		*/

		// 判断是否已经签到
		Map<String, Object> ifSign = new HashMap<String, Object>();
		ifSign.put("username", username);
		ifSign.put("date", date);
		ifSign.put("recd_inout", "in");
		ifSign = this.commonBO.selectOne("com.sie.data.Sign.SelectUserIfSign", ifSign);
		if (ifSign.get("count(*)").toString().equals("0")) {
			resultMap.put("result", "你今天还没有签到！");
			return resultMap;
		}
		//获取上午的签到状态,如果是旷工，则下午也是旷工
		Boolean flage=ifSign.get("work_state").toString().equals("nowork");
		
		
		Map<String, Object> ifSignNoon = new HashMap<String, Object>();
		ifSignNoon.put("username", username);
		ifSignNoon.put("date", date);
		ifSignNoon.put("recd_inout", "out");
		ifSignNoon = this.commonBO.selectOne("com.sie.data.Sign.SelectUserIfSign", ifSignNoon);
		if (!(ifSignNoon.get("count(*)").toString().equals("0"))) {
			resultMap.put("result", "你已经签退过啦！");
			return resultMap;
		}

		// 查询签退的时间,判断是否早退
		Map<String, Object> cheskSigntime = this.commonBO.selectOne("com.sie.data.Sign.SelectCheskSigntime");
		String signState = "nowork";
		if (time.compareTo(cheskSigntime.get("time4").toString())>0
				|| time.compareTo(cheskSigntime.get("time4").toString()) == 0) {
			signState = "normal";
			resultMap.put("result", "签退成功，正常出勤");
		} else {
			if (time.compareTo(cheskSigntime.get("time3").toString())<0
					|| time.compareTo(cheskSigntime.get("time3").toString()) == 0) {
				signState = "nowork";
				resultMap.put("result", "签退成功，状态旷工！");
			} else {
				signState = "early";
				resultMap.put("result", "签退成功，你早退！");
			}
		}
	
		
		if(flage){
			signState = "nowork";
			resultMap.put("result", "签退成功，状态旷工！");
		}
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("username", username);
		params.put("date", date);
		params.put("time", time);
		params.put("real_time", cheskSigntime.get("time4"));
		params.put("work_state", signState);
		params.put("comefrom", cheskSigntime.get("rec_type"));
		this.commonBO.updateOne("com.sie.data.Sign.updateSignNoonRecord", params);
		//System.out.println("修改签退数据成功" + insertSignNoon);
		//下午签退时间在t3前的话,上午也要是旷工
		if("nowork".equals(signState)){
			 this.commonBO.updateOne("com.sie.data.Sign.updateSignMorRecord",params);
			
		}
		return resultMap;
	}

}
