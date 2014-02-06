package com.rootrip.platform.id;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.rootrip.platform.id.util.TextReader;

/**
 * 数据库访问api
 * @author liubin
 * @date 2012-11-12
 *
 */
public class IdCreatorDao {
	private static final Logger log = LoggerFactory.getLogger(IdCreatorDao.class);
	public static final int STEP_LENTH = 500;

	private static final String SQL_CREATE_TABLE = "CREATE TABLE IF NOT EXISTS xproxy_id_server(idxname VARCHAR(64) PRIMARY KEY,curid BIGINT);";
	private static String SQL_GET_NEXT;
	private static final String SQL_NEXT = "call xproxy_id_server_next(?);";
	private DataSource dataSource;
	
	public void init() {
		try {
			SQL_GET_NEXT = TextReader.readJarTxt("/com/rootrip/platform/id/proc.sql", "UTF8");
			createTable();
			createProc();
		} catch (Exception e) {
			log.error("", e);
		}
	}

	/**
	 * 创建表
	 * 
	 * @throws SQLException
	 */
	public void createTable() throws SQLException {
		Connection conn = getConn();
		PreparedStatement pstmt = null;
		try {
			pstmt = conn.prepareStatement(SQL_CREATE_TABLE);
			pstmt.execute();
		} finally {
			closePs(pstmt);
			closeConn(conn);
		}
	}

	/**
	 * 创建存储过程
	 * 
	 * @throws SQLException
	 */
	public void createProc() throws SQLException {
		Connection conn = getConn();
		Statement pstmt = null;
		try {
			pstmt = conn.createStatement();
			pstmt.addBatch(SQL_GET_NEXT);
			pstmt.executeBatch();
		} catch (SQLException e) {
			if(e.getErrorCode() != 1304) {
				//忽略PROCEDURE xproxy_id_server_next already exists的错误
				log.error("", e);
			}
		} finally {
			closeStmt(pstmt);
			closeConn(conn);
		}
	}

	/**
	 * 获取下一跳的起始id
	 * 
	 * @param idname
	 * @return
	 * @throws SQLException
	 */
	public long getNextBase(String idname) throws SQLException {
		Connection conn = getConn();
		CallableStatement pstmt = null;
		try {
			pstmt = conn.prepareCall(SQL_NEXT);
			pstmt.setString(1, idname);
			pstmt.execute();
			ResultSet rs = pstmt.getResultSet();
			rs.next();
			long ret = rs.getLong(1);
			return ret;
		} finally {
			closePs(pstmt);
			closeConn(conn);
		}
	}

	public void closePs(PreparedStatement ps) {
		if (ps != null) {
			try {
				ps.close();
			} catch (Exception e) {
				log.error("", e);
			}
		}
	}

	public void closeStmt(Statement ps) {
		if (ps != null) {
			try {
				ps.close();
			} catch (Exception e) {
				log.error("", e);
			}
		}
	}

	private Connection getConn() throws SQLException {
		return dataSource.getConnection();
	}

	private void closeConn(Connection conn) {
		if (conn != null) {
			try {
				conn.close();
			} catch (Exception e) {
				log.error("", e);
			}
		}
	}

	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
	}

}
