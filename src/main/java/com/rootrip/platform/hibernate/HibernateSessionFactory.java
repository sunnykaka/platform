package com.rootrip.platform.hibernate;

import org.springframework.orm.hibernate3.LocalSessionFactoryBean;

import com.rootrip.platform.Platform;

/**
 * 
 * @author liubin
 *
 */
@SuppressWarnings("unchecked")
public class HibernateSessionFactory extends LocalSessionFactoryBean {

	/*
	 * (non-Javadoc)
	 * @see org.springframework.orm.hibernate3.AbstractSessionFactoryBean#afterPropertiesSet()
	 */
	public void afterPropertiesSet() throws Exception {
		Platform platform = Platform.getInstance();
	    super.setMappingLocations(platform.getHibernateCfgFiles());
	    super.afterPropertiesSet();
	}
	
	
}
