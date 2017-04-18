package com.sie.attend.controller;

import java.text.SimpleDateFormat;
import java.util.Date;

import com.sie.attend.util.FloadNumber;

public class TestTime {

	public static void main(String[] args) {
/*		
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
		}*/
		
		
/*		for(int i=1;i<=50;i++){
            System.out.printf("%03d\n", i); 
}*/
		//System.out.println(FloadNumber.getFloadNumber("2017-04-13", "15:20:30"));
		
		
		
		
		
/*		String excepChange="12&你好-13&很好-18&非常好";
		
		String exId=null;
		String exReason=null;
		String[] id_reasons = excepChange.split("-");
		for (int i = 0; i < id_reasons.length; i++) {
			//System.out.println(id_reasons[i]);
			String[] idAndreason=id_reasons[i].split("&");
				exId=idAndreason[0];
				exReason=idAndreason[1];
				System.out.println("分割出来后的数据"+exId+":"+exReason);
		}
		*/
		String exceptionNum="[\"YCBG20170410002\"]";
		
		System.out.println(exceptionNum.substring(2, exceptionNum.length()-2));
		
		
		

	}

}
