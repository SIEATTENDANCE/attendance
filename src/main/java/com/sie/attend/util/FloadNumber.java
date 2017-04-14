package com.sie.attend.util;

public class FloadNumber {
	
	public static String getFloadNumber(String date,String time){
		
		String flad="YCBG"+date.replace("-", "")+time.replace(":","");
		return flad;
		
	}

}
