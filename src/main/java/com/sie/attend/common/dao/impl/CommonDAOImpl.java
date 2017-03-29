package com.sie.attend.common.dao.impl;

import javax.annotation.Resource;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;

import com.sie.attend.common.dao.CommonDAO;

public class CommonDAOImpl implements CommonDAO {

	@Resource
	private SqlSessionFactory sqlSessionFactory;
	
	@Override
	public SqlSession getSession() {
		return sqlSessionFactory.openSession();
	}

}
