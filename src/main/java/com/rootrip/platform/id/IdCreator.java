package com.rootrip.platform.id;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.rootrip.platform.exception.PlatformException;

/**
 * 特定表的id生成器
 * @author liubin
 * @date 2012-11-12
 *
 */
public class IdCreator {
	private long cur = -1;
	private long end = -2;
	private String name;
	private Object lock = new Object();
	
	private IdCreatorDao dao = null;

	public IdCreator(String name, IdCreatorDao dao) {
		this.name = name;
		this.dao = dao;
	}

	public long getNext() throws PlatformException{
		synchronized (lock) {
			if (cur > end) {
				try {
					load();
				} catch (SQLException e) {
					throw new PlatformException("Fail to load id from db.", e);
				}
			}
			if (cur > end) {
				throw new PlatformException("Fail to load id from db.");
			} else {
				return cur++;
			}
		}
	}

	public List<Range> getIdSet(int count) throws PlatformException{
		if (count < 0)
			throw new IllegalArgumentException(
					"Count of id should not be negative.");

		List<Range> ret = new ArrayList<Range>();
		synchronized (lock) {
			while (count > 0) {
				if (cur == end) {
					try {
						load();
					} catch (SQLException e) {
						throw new PlatformException("Fail to load id from db.", e);
					}
				}
				if (cur == end) {
					throw new PlatformException("Fail to load id from db.");
				} else {
					if (count <= this.end - this.cur + 1) {
						Range r = new Range();
						r.setStart(cur);
						r.setEnd(cur + count - 1);
						ret.add(r);
						count = 0;
					} else {
						Range r = new Range();
						r.setStart(cur);
						r.setEnd(end);
						ret.add(r);
						count -= (end - this.cur + 1);
					}
				}
			}
			return ret;
		}
	}

	private void load() throws SQLException {
		this.cur = dao.getNextBase(this.name);
		this.end = this.cur + IdCreatorDao.STEP_LENTH - 1;
	}
}
