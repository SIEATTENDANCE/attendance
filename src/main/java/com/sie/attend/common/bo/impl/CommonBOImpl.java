package com.sie.attend.common.bo.impl;

import java.util.Map;

import javax.annotation.Resource;

import com.sie.attend.common.bo.CommonBO;
import com.sie.attend.common.dao.CommonDAO;
import com.sie.attend.pojo.User;

public class CommonBOImpl implements CommonBO {
	@Resource
	private CommonDAO commonDao;

	@Override
	public Map<String,Object> selectUser(String sqlId, Map<String, Object> params) {
		return this.commonDao.getSession().selectOne(sqlId, params);
	}

}
