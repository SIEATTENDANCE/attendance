package com.sie.attend.common.bo;

import java.util.List;
import java.util.Map;

public interface CommonBO {
	


	public Map<String,Object> selectOne(String sqlId,Map<String,Object> params);
	public Map<String,Object> selectOne(String sqlId);
	
	//签到模块业务
	public List<Map<String, Object>> selectSignRecord(String sqlId, Map<String, Object> params);
	public Map<String,Object> selectIfSign(String sqlId,Map<String,Object> params);	
	
}
