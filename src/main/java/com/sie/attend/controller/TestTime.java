package com.sie.attend.controller;

public class TestTime {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String date = "2017-03-05";
		String time = "12:30:28";
		System.out.println();
		
		System.out.println(time.compareTo("8:00:00"));
		System.out.println(time.compareTo("12:00:00"));
		if (time.compareTo("8:00:00") ==-1 || time.compareTo("8:00:00") == 0) {
			System.out.println("签到成功，正常出勤");
		} else {
				
			if (time.compareTo("12:00:00")==1) {
				System.out.println("签到成功，状态旷工！");
				System.out.println(time.compareTo("12:00:00")==1);
			}else {
				System.out.println("签到成功，迟到！");
				System.out.println(time.compareTo("12:00:00")==1);
			}

		}

	}

}
