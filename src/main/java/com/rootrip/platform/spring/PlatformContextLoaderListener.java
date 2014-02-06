package com.rootrip.platform.spring;

import javax.servlet.ServletContextEvent;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.ContextLoaderListener;

/**
 * 
 * @author liubin
 * @date 2012-11-9
 *
 */
@SuppressWarnings("unchecked")
public class PlatformContextLoaderListener extends ContextLoaderListener {
	
	protected final Logger logger = LoggerFactory.getLogger(getClass());

	@Override
	public void contextInitialized(ServletContextEvent event) {
		String strCtxLoaderClazz = event.getServletContext().getInitParameter("loader");
		if(strCtxLoaderClazz != null && !"".equals(strCtxLoaderClazz)) {
			try {
				ctxLoaderClazz = Class.forName(strCtxLoaderClazz.trim());
			} catch (ClassNotFoundException e) {
				logger.error("缺少" + ctxLoaderClazz + ",使用默认的PlatformContextLoader.", e);
			}
		}
		super.contextInitialized(event);
	}

	// SpringContextLoader的实现类
	private Class ctxLoaderClazz;
	
	/**
	 * encode name
	 */
	protected static final String ENCODE = "encode";

	/**
	 * 字符集
	 */
	protected String encode = "UTF-8";
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 4867238482018895923L;

	/*
	 * (non-Javadoc)
	 * @see org.springframework.web.context.ContextLoaderServlet#createContextLoader()
	 */
	@Override
	protected ContextLoader createContextLoader() {
		if(ctxLoaderClazz != null) {
			try {
				ContextLoader loader = (ContextLoader)ctxLoaderClazz.newInstance();
				return loader;
			} catch (Exception e) {
				logger.error(ctxLoaderClazz + "初始化失败,使用PlatformContextLoader.", e);
			}
		}
		return new PlatformContextLoader();
	}
}


