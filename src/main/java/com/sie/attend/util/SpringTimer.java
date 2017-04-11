package com.sie.attend.util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

import com.sie.attend.common.bo.CommonBO;

public class SpringTimer {

	@Resource(name = "commonBO")
	private CommonBO commonBO;

	// public static int searchIndexFlag = 0;//由tiggertimer修改，其他方法不要修改此参数
	private Lock lock = new ReentrantLock();// 锁对象

	public void tiggertimer() {

		lock.lock();
		// 保证同一时刻只有一个定时器运行,通过这种方式保证每次定时时间到时，只执行一个线程

		Map<String, Object> params = new HashMap<>();
		String date = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
		params.put("date", date);

		params = this.commonBO.selectOne("com.sie.data.Timer.selectEmployeeCount");// 获取员工总的数量
		long employeeNum = (long) params.get("count");
		System.out.println(employeeNum);
		for (int i = 1; i <= employeeNum; i++) {
			params.put("userId", i);
			params = this.commonBO.selectOne("com.sie.data.Timer.selectIfsign", params);

			if ((long) params.get("count") != 0) {
				// 说明今天没有来上班，设置为旷工
				String username = (String) params.get("emp_id");
				params.put("username", username);
				params = this.commonBO.selectOne("com.sie.data.Timer.insertSignMorRecord", params);
				params = this.commonBO.selectOne("com.sie.data.Timer.insertSignNoonRecord", params);
			}

		}

		lock.unlock();

	}

}