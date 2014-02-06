package com.rootrip.platform.jdbc;

import java.sql.Connection;
import java.sql.SQLException;

import javax.sql.DataSource;

/**
 * 
 * @author liubin
 * @date 2013-1-21
 *
 */
public class ConnectionProvider {

	// 数据源
	private DataSource dataSource;

	/**
	 * @return the dataSource
	 */
	public final DataSource getDataSource() {
		return dataSource;
	}

	/**
	 * @param dataSource the dataSource to set
	 */
	public final void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
	}
	
	/**
	 * 取得Connection对象
	 * @return Connection对象
	 * @throws SQLException 异常
	 */
	public Connection getConnection() throws SQLException{
		return dataSource.getConnection();
	}
	
}
