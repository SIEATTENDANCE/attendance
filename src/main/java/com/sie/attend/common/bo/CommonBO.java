package com.sie.attend.common.bo;

import java.util.List;
import java.util.Map;

public interface CommonBO {
	


	public Map<String,Object> selectOne(String sqlId,Map<String,Object> params);
	public Map<String,Object> selectOne(String sqlId);
	public List<Map<String, Object>> selectALL(String string, Map<String, Object> params);
	
	//签到模块业务
	public List<Map<String, String>> selectSignRecord(String sqlId, Map<String, Object> params);//查询单个人的签到记录
	public Map<String,Object> selectIfSign(String sqlId,Map<String,Object> params);	//判断是否有签到
	public Map<String, Object> selectCheskSigntime(String sqlId);//查询正常的签到时间
	public int insertSignMor(String sqlId, Map<String, Object> params);
	public int insertSignNoon(String sqlId, Map<String, Object> params);
	public int updateSignNoon(String string, Map<String, Object> params);
	public List<Map<String, Object>> selectList(String sqlId, Map<String, Object> params);
	
	
}
