package com.sie.attend.common.dao;

import org.apache.ibatis.session.SqlSession;

public interface CommonDAO {
	
	public SqlSession getSession();
}
