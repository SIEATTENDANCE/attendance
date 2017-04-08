package com.sie.attend.common.bo.impl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.sie.attend.common.bo.CommonBO;
import com.sie.attend.common.dao.CommonDAO;

public class CommonBOImpl implements CommonBO {
	@Resource(name="commonDao")
	private CommonDAO commonDao;

	@Override
	public Map<String, Object> selectOne(String sqlId, Map<String, Object> params) {
		return this.commonDao.getSession().selectOne(sqlId, params);
	}
	@Override
	public Map<String, Object> selectOne(String sqlId) {
		// TODO Auto-generated method stub
		return this.commonDao.getSession().selectOne(sqlId);
	}
	@Override
	public List<Map<String, Object>> selectSignRecord(String sqlId, Map<String, Object> params) {
		return this.commonDao.getSession().selectList(sqlId, params);
	}
	@Override
	public Map<String, Object> selectIfSign(String sqlId, Map<String, Object> params) {
		// TODO Auto-generated method stub
		return this.commonDao.getSession().selectOne(sqlId, params);
	}



}
