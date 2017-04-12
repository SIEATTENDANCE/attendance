package com.sie.attend.controller;

import java.text.SimpleDateFormat;
import java.util.Date;

public class TestTime {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		SimpleDateFormat TimeStyle = new SimpleDateFormat("HH:mm:ss");
		
		String time = TimeStyle.format(new Date());
		System.out.println(time);
		time = "08:50:28";
		System.out.println(time.compareTo("08:00:00"));
		System.out.println(time.compareTo("12:00:00"));
		if (time.compareTo("08:00:00")<0 || time.compareTo("08:00:00") == 0) {
			System.out.println("签到成功，正常出勤");
		} else {	
			if (time.compareTo("12:00:00")>0) {
				System.out.println("签到成功，状态旷工！");
				System.out.println(time.compareTo("12:00:00")==1);
			}else {
				System.out.println("签到成功，迟到！");
				System.out.println(time.compareTo("12:00:00")==-1);
			}

		}

	}

}
