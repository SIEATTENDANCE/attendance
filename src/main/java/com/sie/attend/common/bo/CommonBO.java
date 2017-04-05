package com.sie.attend.common.bo;

import java.util.Map;

public interface CommonBO {
	
//	public Map<String,Object> selectUser(String sqlId,Map<String,Object> params);

	public Map<String,Object> selectOne(String sqlId,Map<String,Object> params);
}
