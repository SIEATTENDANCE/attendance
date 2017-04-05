package com.sie.attend.common.bo;

import java.util.Map;

public interface CommonBO {
	
//	public Map<String,Object> selectUser(String sqlId,Map<String,Object> params);
	
	/*
	 * 根据编号和密码查询
	 */
	public Map<String,Object> selectOne(String sqlId,Map<String,Object> params);
}
