package com.rootrip.platform.common.dao;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.hibernate.LockMode;
import org.hibernate.Query;

/**
 * 查询锁的策略
 * 使用锁对hql有一定限制.
 * 首先查询的实体都需要使用别名.例如select hc from HyperCloud hc
 * 其次查询结果必须是对象,例如前面的实例,而不能是select hc.id from HyperCloud hc
 * @author liubin
 *
 */
public class LockOptions {

	private Map<String, LockMode> lockModeMap = new HashMap<String, LockMode>();
	// 超时时间,单位是秒
	private Integer timeout;
	
	// 单独的lockMode
	private LockMode lockMode;
	
	private LockOptions() {}

	/**
	 * 工厂方法
	 * @param alias 查询实体的别名,例如对于select hc from HyperCloud hc left join hc.proxyServer ps这条hql语句,alias需要传hc
	 * @param lockMode 锁策略
	 * @param timeout 超时时间,单位是秒
	 * @return
	 */
	public static final LockOptions create(String alias, LockMode lockMode, Integer timeout) {
		LockOptions options = new LockOptions();
		options.set(alias, lockMode, timeout);
		return options;
	}
	
	/**
	 * 工厂方法
	 * @param alias 查询实体的别名,例如对于select hc from HyperCloud hc left join hc.proxyServer ps这条hql语句,alias需要传hc
	 * @param lockMode 锁策略
	 * @return
	 */
	public static final LockOptions create(String alias, LockMode lockMode) {
		return create(alias, lockMode, null);
	}
	
	/**
	 * 工厂方法
	 * @param lockMode 锁策略
	 * @return
	 */
	public static final LockOptions create(LockMode lockMode) {
		LockOptions options = new LockOptions();
		options.lockMode = lockMode;
		return options;
	}
	
	/**
	 * @param alias 查询实体的别名,例如对于select hc from HyperCloud hc left join hc.proxyServer ps这条hql语句,alias需要传hc
	 * @param lockMode 锁策略
	 * @param timeout 超时时间,单位是秒
	 * @return
	 */
	public LockOptions set(String alias, LockMode lockMode, Integer timeout) {
		if(alias != null && lockMode != null) {
			lockModeMap.put(alias, lockMode);
		}
		if(timeout != null) {
			this.timeout = timeout;
		}
		return this;
	}
	
	/**
	 * @param timeout 超时时间,单位是秒
	 * @return
	 */
	public LockOptions set(int timeout) {
		return this.set(null, null, timeout);
	}
	
	/**
	 * @param alias 查询实体的别名,例如对于select hc from HyperCloud hc left join hc.proxyServer ps这条hql语句,alias需要传hc
	 * @param lockMode 锁策略
	 * @return
	 */
	public LockOptions set(String alias, LockMode lockMode) {
		return this.set(alias, lockMode, null);
	}

	public Map<String, LockMode> getLockModeMap() {
		return lockModeMap;
	}

	public Integer getTimeout() {
		return timeout;
	}

	/**
	 * @param query
	 */
	public void handleQuery(Query query) {
		if(query == null) return;
		if(!lockModeMap.isEmpty()) {
			for(Entry<String, LockMode> entry : lockModeMap.entrySet()) {
				query.setLockMode(entry.getKey(), entry.getValue());
			}
		}
		if(timeout != null) query.setTimeout(timeout);
	}
	
	public LockMode getLockMode() {
		return lockMode;
	}
	
}
