package com.sie.attend.util;

public class FloadNumber {
	
	String sdate=null;
	static String count="000";
	
	public static String getFloadNumber(String date){
		
		String flad="YCBG"+date.replace("-", "")+count;
		return flad;
		
	}

}
