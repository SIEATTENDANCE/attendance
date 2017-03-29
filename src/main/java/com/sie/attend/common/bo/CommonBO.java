package com.sie.attend.common.bo;

import java.util.Map;

import com.sie.attend.pojo.User;

public interface CommonBO {
	public User selectUser(String sqlId,Map<String,Object> params);
}
