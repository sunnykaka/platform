package com.rootrip.platform.test;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;
import org.springframework.test.context.transaction.TransactionConfiguration;

/**
 * Junit单元测试基类
 * @author liubin
 * @date 2012-11-12
 *
 */
@ContextConfiguration(loader=PlatformTestContextLoader.class)
@TransactionConfiguration(transactionManager="transactionManager", defaultRollback=false)
public abstract class PlatformTest extends AbstractTransactionalJUnit4SpringContextTests {
	
	protected Logger log = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	protected SessionFactory sessionFactory;

	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}
	
	protected void flushSessionToCheckStates() {
		Session session = this.sessionFactory.getCurrentSession();
		if(session != null) {
			session.flush();
		}
	}
}
