package com.rootrip.platform.incrementer;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.sql.DataSource;

import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataAccessResourceFailureException;
import org.springframework.jdbc.datasource.DataSourceUtils;
import org.springframework.jdbc.support.JdbcUtils;
import org.springframework.jdbc.support.incrementer.MySQLMaxValueIncrementer;

public class PlatformMaxValueIncrementer extends MySQLMaxValueIncrementer {
	
	public PlatformMaxValueIncrementer() {
		super();
	}

	public PlatformMaxValueIncrementer(DataSource dataSource,
			String incrementerName, String columnName) {
		super(dataSource, incrementerName, columnName);
	}

	/** The SQL string for retrieving the new sequence value */
	private static final String VALUE_SQL = "select last_insert_id()";

	/** The next id to serve */
	private long nextId = 0;

	/** The max id to serve */
	private long maxId = 0;
	
	@Override
	protected synchronized long getNextKey() throws DataAccessException {
		if (this.maxId == this.nextId) {
			/*
			* Need to use straight JDBC code because we need to make sure that the insert and select
			* are performed on the same connection (otherwise we can't be sure that last_insert_id()
			* returned the correct value)
			*/
			Statement stmt = null;
			Connection con = null;
			boolean isAutoCommit = false;
			try {
				con = getDataSource().getConnection();
				isAutoCommit = con.getAutoCommit();
				con.setAutoCommit(false);
				stmt = con.createStatement();
				DataSourceUtils.applyTransactionTimeout(stmt, getDataSource());
				// Increment the sequence column...
				String columnName = getColumnName();
				stmt.executeUpdate("update "+ getIncrementerName() + " set " + columnName +
						" = last_insert_id(" + columnName + " + " + getCacheSize() + ")");
				// Retrieve the new max of the sequence column...
				ResultSet rs = stmt.executeQuery(VALUE_SQL);
				try {
					if (!rs.next()) {
						throw new DataAccessResourceFailureException("last_insert_id() failed after executing an update");
					}
					this.maxId = rs.getLong(1);
				}
				finally {
					JdbcUtils.closeResultSet(rs);
				}
				this.nextId = this.maxId - getCacheSize() + 1;
				con.commit();
			}
			catch (SQLException ex) {
				throw new DataAccessResourceFailureException("Could not obtain last_insert_id()", ex);
			}
			finally {
				if(con != null) {
					try {
						con.setAutoCommit(isAutoCommit);
					} catch (SQLException e) {
						throw new DataAccessResourceFailureException("Resume auto commit error", e);
					}
				}
				JdbcUtils.closeStatement(stmt);
				DataSourceUtils.releaseConnection(con, getDataSource());
			}
		}
		else {
			this.nextId++;
		}
		return this.nextId;
	}
}
