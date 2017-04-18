package com.sie.attend.util;

public class FloadNumber {
	
	public static String getFloadNumber(String date,String time){
		String ctime=time.replace(":","");
		String flad="YCBG"+date.replace("-", "")+ctime.substring(0,ctime.length()-2);
		return flad;
	}
}
