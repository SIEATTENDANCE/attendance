package com.sie.attend.common.bo.impl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.sie.attend.common.bo.CommonBO;
import com.sie.attend.common.dao.CommonDAO;

public class CommonBOImpl implements CommonBO {
	@Resource(name = "commonDao")
	private CommonDAO commonDao;

	@Override
	public Map<String, Object> selectOne(String sqlId) {
		return this.commonDao.getSession().selectOne(sqlId);
	}

	@Override
	public Map<String, Object> selectOne(String sqlId, Map<String, Object> params) {
		return this.commonDao.getSession().selectOne(sqlId, params);
	}

	@Override
	public List<Map<String, Object>> selectList(String sqlId) {
		return this.commonDao.getSession().selectList(sqlId);
	}

	@Override
	public List<Map<String, Object>> selectList(String sqlId, Map<String, Object> params) {
		return this.commonDao.getSession().selectList(sqlId, params);
	}

	@Override
	public int insertOne(String sqlId, Map<String, Object> params) {
		return this.commonDao.getSession().insert(sqlId, params);
	}

	@Override
	public int updateOne(String string, Map<String, Object> params) {
		return this.commonDao.getSession().update(string, params);
	}

}
